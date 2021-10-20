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

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate template;

    /**
     * Happy path through REST apis.
     */
    @Test
    void lifecycle() throws JsonProcessingException {
        final Contact newContact = Contact.builder()
                .name("Pepe King Prawn")
                .phone("12345")
                .build();
        final ResponseEntity<String> postResponse = template.postForEntity(buildUrl("contacts"), newContact, String.class);
        assertTrue(postResponse.getStatusCode().is2xxSuccessful());

        List<Map<String, String>> allContactsAfterAdd = getAllContacts();
        assertEquals(1, allContactsAfterAdd.size());

        final Map<String, String> contact = allContactsAfterAdd.get(0);
        assertNotNull(contact.get("id"));
        assertEquals(newContact.getName(), contact.get("name"));
        assertEquals(newContact.getPhone(), contact.get("phone"));

        final ResponseEntity<String> deleteResponse = template.exchange(buildUrl("contacts/1"), HttpMethod.DELETE, null, String.class);
        assertTrue(deleteResponse.getStatusCode().is2xxSuccessful());

        List<Map<String, String>> allContactsAfterDelete = getAllContacts();
        assertEquals(0, allContactsAfterDelete.size());
    }

    private List<Map<String, String>> getAllContacts() throws JsonProcessingException {
        final ResponseEntity<String> getResponse = template.getForEntity(buildUrl("contacts"), String.class);
        assertTrue(getResponse.getStatusCode().is2xxSuccessful());

        final ObjectMapper mapper = new ObjectMapper();
        @SuppressWarnings("unchecked")
        List<Map<String, String>> allContacts = mapper.readValue(getResponse.getBody(), ArrayList.class);
        return allContacts;
    }

    private String buildUrl(final String path) {
        return "http://localhost:" + port + "/" + path;
    }
}