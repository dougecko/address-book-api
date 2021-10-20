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
import shine.aba.model.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {Application.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerIT {

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
        final List<Map<String, Object>> allBooksBeforeAdd = getAllBooks();
        assertEquals(0, allBooksBeforeAdd.size());

        final Book newBook = Book.builder()
                .name("What a book!")
                .build();
        createBook(newBook);

        final List<Map<String, Object>> allBooksAfterAdd = getAllBooks();
        assertEquals(1, allBooksAfterAdd.size());

        final Map<String, Object> firstAddedBook = allBooksAfterAdd.get(0);
        final Integer newId = (Integer) firstAddedBook.get("id");
        assertNotNull(newId);
        assertEquals(newBook.getName(), firstAddedBook.get("name"));

        final String newBookPath = "books/" + newId;

        // test second endpoint to return same new book
        final ResponseEntity<String> getResponse = template.getForEntity(buildUrl(newBookPath), String.class);
        assertTrue(getResponse.getStatusCode().is2xxSuccessful());
        final Book addedBook = OBJECT_MAPPER.readValue(getResponse.getBody(), Book.class);
        assertEquals(String.valueOf(firstAddedBook.get("id")), String.valueOf(addedBook.getId()));
        assertEquals(firstAddedBook.get("name"), addedBook.getName());

        final ResponseEntity<String> deleteResponse = template.exchange(buildUrl(newBookPath), HttpMethod.DELETE, null, String.class);
        assertTrue(deleteResponse.getStatusCode().is2xxSuccessful());

        final List<Map<String, Object>> allBooksAfterDelete = getAllBooks();
        assertEquals(0, allBooksAfterDelete.size());
    }

    /**
     * Ensure that BookNotFoundException is converted to 404 HTTP status code.
     */
    @Test
    void getBook_withUnknownId_throws404() {
        final ResponseEntity<String> response = template.getForEntity(buildUrl("books/99"), String.class);
        assertEquals(404, response.getStatusCodeValue());
    }

    /**
     * Ensure that BookAlreadyExistsException is converted to 400 HTTP status code.
     */
    @Test
    void addBook_withExistingId_throws400() throws JsonProcessingException {
        final Book firstBook = Book.builder()
                .name("Book the First")
                .build();
        // let the DB assign an id
        final Long bookId = createBook(firstBook);

        final Book secondBook = Book.builder()
                .name("Book the Second")
                .build();
        // use the same id as the first book
        secondBook.setId(bookId);

        final ResponseEntity<String> response = template.postForEntity(buildUrl("books"), secondBook, String.class);
        assertEquals(400, response.getStatusCodeValue());
    }

    /**
     * Ensure that validation exception is converted to 400 HTTP status code.
     */
    @Test
    void addBook_withTooLongName_throws400() {
        final Book book = Book.builder()
                .name("This name is too long this name is too long this name is too long this name is too long this name is too long this name is too long this name is too long this name is too long this name is too long this name is too long this name is too long this name is too long")
                .build();

        final ResponseEntity<String> response = template.postForEntity(buildUrl("books"), book, String.class);
        assertEquals(400, response.getStatusCodeValue());
    }

    /**
     * Ensure that validation exception is converted to 400 HTTP status code.
     */
    @Test
    void addBook_withMissingName_throws400() {
        final Book book = Book.builder().build();

        final ResponseEntity<String> response = template.postForEntity(buildUrl("books"), book, String.class);
        assertEquals(400, response.getStatusCodeValue());
    }

    private Long createBook(Book book) throws JsonProcessingException {
        final ResponseEntity<String> response = template.postForEntity(buildUrl("books"), book, String.class);
        assertTrue(response.getStatusCode().is2xxSuccessful());

        return OBJECT_MAPPER.readValue(response.getBody(), Book.class).getId();
    }

    private List<Map<String, Object>> getAllBooks() throws JsonProcessingException {
        final ResponseEntity<String> getResponse = template.getForEntity(buildUrl("books"), String.class);
        assertTrue(getResponse.getStatusCode().is2xxSuccessful());

        //noinspection unchecked
        return OBJECT_MAPPER.readValue(getResponse.getBody(), ArrayList.class);
    }

    private String buildUrl(final String path) {
        return IntegrationTestUtil.buildUrl(port, path);
    }
}