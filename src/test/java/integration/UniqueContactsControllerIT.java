package integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import shine.aba.Application;
import shine.aba.model.Book;
import shine.aba.model.Contact;
import shine.aba.model.UniqueContactsRequest;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = {Application.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UniqueContactsControllerIT {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate template;

    /**
     * Set up two books, with two contacts in each, one of which appears in both.
     * Verify that the response from getUniqueContacts() only contains that contact once.
     */
    @Test
    @Sql("/resetDb.sql")
    void getUniqueContacts_withDuplicates_succeeds() throws JsonProcessingException {
        final Contact contact1 = createContact(Contact.builder()
                .name("Pepe King Prawn")
                .phone("12345")
                .build());

        final Contact contact2 = createContact(Contact.builder()
                .name("Rizzo the Rat")
                .phone("54321")
                .build());

        final Book book1 = Book.builder()
                .name("Muppets Tonight")
                .build();
        book1.setContacts(Set.of(contact1, contact2));
        final long bookId1 = createBook(book1).getId();

        final Contact contact3 = createContact(Contact.builder()
                .name("Kermit Frog")
                .phone("7284 9284")
                .build());

        final Book book2 = Book.builder()
                .name("The Muppet Movie")
                .build();
        book2.setContacts(Set.of(contact2, contact3));
        final long bookId2 = createBook(book2).getId();

        final UniqueContactsRequest request = new UniqueContactsRequest();
        request.setBookIds(List.of(bookId1, bookId2));

        final ResponseEntity<String> postResponse = template.postForEntity(buildUrl("uniquecontacts"), request, String.class);
        assertTrue(postResponse.getStatusCode().is2xxSuccessful());

        final List<Contact> uniqueContacts = OBJECT_MAPPER.readValue(postResponse.getBody(), new TypeReference<>() {
        });
        // ensure that the rat doesn't appear twice in final set, even though it's included in both books
        assertEquals(3, uniqueContacts.size());

        // since everything is already nicely set up, test the "contacts in specified book" endpoint as well
        final ResponseEntity<String> getResponse = template.getForEntity(buildUrl("books/" + bookId1 + "/contacts"), String.class);
        assertTrue(getResponse.getStatusCode().is2xxSuccessful());

        final List<Contact> bookContacts = OBJECT_MAPPER.readValue(getResponse.getBody(), new TypeReference<>() {
        });
        assertEquals(2, bookContacts.size());
        assertTrue(bookContacts.stream().anyMatch(contact -> "Pepe King Prawn".equals(contact.getName())));
        assertTrue(bookContacts.stream().anyMatch(contact -> "Rizzo the Rat".equals(contact.getName())));
    }

    private Contact createContact(Contact contact) throws JsonProcessingException {
        final ResponseEntity<String> response = template.postForEntity(buildUrl("contacts"), contact, String.class);
        assertTrue(response.getStatusCode().is2xxSuccessful());

        return OBJECT_MAPPER.readValue(response.getBody(), Contact.class);
    }

    private Book createBook(Book book) throws JsonProcessingException {
        final ResponseEntity<String> response = template.postForEntity(buildUrl("books"), book, String.class);
        assertTrue(response.getStatusCode().is2xxSuccessful());

        return OBJECT_MAPPER.readValue(response.getBody(), Book.class);
    }

    private String buildUrl(String path) {
        return IntegrationTestUtil.buildUrl(port, path);
    }
}
