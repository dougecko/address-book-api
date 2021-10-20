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
import shine.aba.Application;
import shine.aba.model.Contact;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {Application.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AddressBookControllerIT {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate template;

    /**
     * Happy path through REST apis.
     */
    @Test
    void lifecycle() throws JsonProcessingException {
        final List<Map<String, Object>> allContactsBeforeAdd = getAllContacts();
        assertEquals(0, allContactsBeforeAdd.size());

        final Contact newContact = Contact.builder()
                .name("Pepe King Prawn")
                .phone("12345")
                .build();
        final ResponseEntity<String> postResponse = template.postForEntity(buildUrl("contacts"), newContact, String.class);
        assertTrue(postResponse.getStatusCode().is2xxSuccessful());

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

    private List<Map<String, Object>> getAllContacts() throws JsonProcessingException {
        final ResponseEntity<String> getResponse = template.getForEntity(buildUrl("contacts"), String.class);
        assertTrue(getResponse.getStatusCode().is2xxSuccessful());

        @SuppressWarnings("unchecked")
        final List<Map<String, Object>> allContacts = OBJECT_MAPPER.readValue(getResponse.getBody(), ArrayList.class);
        return allContacts;
    }

    private String buildUrl(final String path) {
        return "http://localhost:" + port + "/" + path;
    }
}