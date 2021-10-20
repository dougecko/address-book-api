package integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import shine.aba.Application;
import shine.aba.model.Contact;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {Application.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ContactControllerIT {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate template;

    /**
     * Complex, but happy, path through REST apis.
     */
    @Test
    @Sql("/resetDb.sql")
    void fullLifecycle_withValidParameters_succeeds() throws JsonProcessingException {
        final List<Map<String, Object>> allContactsBeforeAdd = getAllContacts();
        assertEquals(0, allContactsBeforeAdd.size());

        final Contact newContact = Contact.builder()
                .name("Pepe King Prawn")
                .phone("12345")
                .build();
        createContact(newContact);

        final List<Map<String, Object>> allContactsAfterAdd = getAllContacts();
        assertEquals(1, allContactsAfterAdd.size());

        final Map<String, Object> firstAddedContact = allContactsAfterAdd.get(0);
        final Integer newId = (Integer) firstAddedContact.get("id");
        assertNotNull(newId);
        assertEquals(newContact.getName(), firstAddedContact.get("name"));
        assertEquals(newContact.getPhone(), firstAddedContact.get("phone"));

        final String newContactPath = "contacts/" + newId;

        // test second endpoint to return same new contact
        final ResponseEntity<String> getResponse = template.getForEntity(buildUrl(newContactPath), String.class);
        assertTrue(getResponse.getStatusCode().is2xxSuccessful());
        final Contact addedContact = OBJECT_MAPPER.readValue(getResponse.getBody(), Contact.class);
        assertEquals(String.valueOf(firstAddedContact.get("id")), String.valueOf(addedContact.getId()));
        assertEquals(firstAddedContact.get("name"), addedContact.getName());
        assertEquals(firstAddedContact.get("phone"), addedContact.getPhone());

        final ResponseEntity<String> deleteResponse = template.exchange(buildUrl(newContactPath), HttpMethod.DELETE, null, String.class);
        assertTrue(deleteResponse.getStatusCode().is2xxSuccessful());

        final List<Map<String, Object>> allContactsAfterDelete = getAllContacts();
        assertEquals(0, allContactsAfterDelete.size());
    }

    /**
     * Ensure that ContactNotFoundException is converted to 404 HTTP status code.
     */
    @Test
    void getContact_withUnknownId_throws404() {
        final ResponseEntity<String> response = template.getForEntity(buildUrl("contacts/99"), String.class);
        assertEquals(404, response.getStatusCodeValue());
    }

    /**
     * Ensure that ContactAlreadyExistsException is converted to 400 HTTP status code.
     */
    @Test
    void addContact_withExistingId_throws400() throws JsonProcessingException {
        final Contact firstContact = Contact.builder()
                .name("Pepe King Prawn")
                .phone("12345")
                .build();
        // let the DB assign an id
        final Long contactId = createContact(firstContact);

        final Contact secondContact = Contact.builder()
                .name("Rizzo the Rat")
                .phone("54321")
                .build();
        // use the same id as the first contact
        secondContact.setId(contactId);

        final ResponseEntity<String> response = template.postForEntity(buildUrl("contacts"), secondContact, String.class);
        assertEquals(400, response.getStatusCodeValue());
    }

    /**
     * Ensure that validation exception is converted to 400 HTTP status code.
     */
    @Test
    void addContact_withTooLongName_throws400() {
        final Contact contact = Contact.builder()
                .name("This name is too long this name is too long this name is too long this name is too long this name is too long this name is too long this name is too long this name is too long this name is too long this name is too long this name is too long this name is too long")
                .phone("12345")
                .build();

        final ResponseEntity<String> response = template.postForEntity(buildUrl("contacts"), contact, String.class);
        assertEquals(400, response.getStatusCodeValue());
    }

    /**
     * Ensure that validation exception is converted to 400 HTTP status code.
     */
    @Test
    void addContact_withMissingName_throws400() {
        final Contact contact = Contact.builder()
                .phone("12345")
                .build();

        final ResponseEntity<String> response = template.postForEntity(buildUrl("contacts"), contact, String.class);
        assertEquals(400, response.getStatusCodeValue());
    }

    /**
     * Ensure that validation exception is converted to 400 HTTP status code.
     */
    @Test
    void addContact_withInvalidPhone_throws400() {
        final Contact contact = Contact.builder()
                .name("Guy Chapman")
                .phone("not a phone number")
                .build();

        final ResponseEntity<String> response = template.postForEntity(buildUrl("contacts"), contact, String.class);
        assertEquals(400, response.getStatusCodeValue());
    }

    /**
     * Ensure that validation exception is converted to 400 HTTP status code.
     */
    @Test
    void addContact_withMissingPhone_throws400() {
        final Contact contact = Contact.builder()
                .name("Guy Chapman")
                .build();

        final ResponseEntity<String> response = template.postForEntity(buildUrl("contacts"), contact, String.class);
        assertEquals(400, response.getStatusCodeValue());
    }

    private Long createContact(Contact contact) throws JsonProcessingException {
        final ResponseEntity<String> response = template.postForEntity(buildUrl("contacts"), contact, String.class);
        assertTrue(response.getStatusCode().is2xxSuccessful());

        return OBJECT_MAPPER.readValue(response.getBody(), Contact.class).getId();
    }

    private List<Map<String, Object>> getAllContacts() throws JsonProcessingException {
        final ResponseEntity<String> getResponse = template.getForEntity(buildUrl("contacts"), String.class);
        assertTrue(getResponse.getStatusCode().is2xxSuccessful());

        //noinspection unchecked
        return OBJECT_MAPPER.readValue(getResponse.getBody(), ArrayList.class);
    }

    private String buildUrl(final String path) {
        return "http://localhost:" + port + "/" + path;
    }
}