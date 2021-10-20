package shine.aba.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import shine.aba.exception.BookAlreadyExistsException;
import shine.aba.exception.BookNotFoundException;
import shine.aba.model.Book;
import shine.aba.repository.BookRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class BookServiceTest {

    private static final long BOOK_ID = 1;
    private static final long ANOTHER_BOOK_ID = 2;
    private static final Book EXISTING_BOOK;
    private static final Book NEW_BOOK;
    private static final Book ANOTHER_BOOK;

    static {
        EXISTING_BOOK = Book.builder()
                .name("The Red Book")
                .build();
        EXISTING_BOOK.setId(BOOK_ID);

        NEW_BOOK = Book.builder()
                .name("It's Actually Blue")
                .build();
        NEW_BOOK.setId(BOOK_ID);

        ANOTHER_BOOK = Book.builder()
                .name("Some Other Book")
                .build();
        EXISTING_BOOK.setId(ANOTHER_BOOK_ID);
    }

    @Mock
    private BookRepository repository;

    @InjectMocks
    private BookService service;

    @Test
    void addBook_whenBookDoesNotExist_succeeds() {
        when(repository.findById(BOOK_ID)).thenReturn(Optional.empty());

        when(repository.save(any())).thenReturn(NEW_BOOK);

        final Book addedBook = service.addBook(NEW_BOOK);
        assertEquals(NEW_BOOK, addedBook);
    }

    @Test
    void addBook_whenBookExists_throwsException() {
        when(repository.findById(BOOK_ID)).thenReturn(Optional.of(EXISTING_BOOK));

        assertThrows(BookAlreadyExistsException.class, () -> service.addBook(NEW_BOOK));
    }

    @Test
    void updateBook_whenBookExists_succeeds() {
        when(repository.findById(BOOK_ID)).thenReturn(Optional.of(EXISTING_BOOK));

        when(repository.save(any())).thenReturn(NEW_BOOK);

        final Book updatedBook = service.updateBook(NEW_BOOK);
        assertEquals(NEW_BOOK, updatedBook);
    }

    @Test
    void updateBook_whenBookDoesNotExist_throwsException() {
        when(repository.findById(BOOK_ID)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> service.updateBook(NEW_BOOK));
    }

    @Test
    void updateBook_whenIdIsNull_throwsException() {
        final Book nullIdBook = cloneWithoutId(NEW_BOOK);

        assertThrows(BookNotFoundException.class, () -> service.updateBook(nullIdBook));
    }

    @Test
    void removeBook_whenBookExists_succeeds() {
        // allow call to succeed
        Mockito.doNothing().when(repository).deleteById(BOOK_ID);

        // no assertion possible on void, but test will fail if any exception thrown
        service.removeBook(BOOK_ID);
    }

    @Test
    void removeBook_whenBookDoesNotExist_throwsException() {
        Mockito.doThrow(new EmptyResultDataAccessException(1)).when(repository).deleteById(BOOK_ID);

        assertThrows(BookNotFoundException.class, () -> service.removeBook(BOOK_ID));
    }

    @Test
    void findBook_whenBookExists_succeeds() {
        when(repository.findById(BOOK_ID)).thenReturn(Optional.of(EXISTING_BOOK));

        final Book foundBook = service.findBook(BOOK_ID);
        assertEquals(EXISTING_BOOK, foundBook);
    }

    @Test
    void findBook_whenBookDoesNotExist_throwsException() {
        when(repository.findById(BOOK_ID)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> service.findBook(BOOK_ID));
    }

    @Test
    void findAllBooks_whenOneBookExists_succeeds() {
        when(repository.findAll()).thenReturn(Collections.singletonList(EXISTING_BOOK));

        final List<Book> foundBook = service.findAllBooks();
        assertEquals(1, foundBook.size());
        assertEquals(EXISTING_BOOK, foundBook.get(0));
    }

    @Test
    void findAllBooks_whenTwoBooksExist_succeeds() {
        when(repository.findAll()).thenReturn(List.of(EXISTING_BOOK, ANOTHER_BOOK));

        final List<Book> foundBooks = service.findAllBooks();
        assertEquals(2, foundBooks.size());
        assertEquals(EXISTING_BOOK, foundBooks.get(0));
        assertEquals(ANOTHER_BOOK, foundBooks.get(1));
    }

    @Test
    void findAllBooks_whenNoBooksExist_succeeds() {
        when(repository.findAll()).thenReturn(new ArrayList<>());

        final List<Book> foundBooks = service.findAllBooks();
        assertEquals(0, foundBooks.size());
    }

    private static Book cloneWithoutId(@SuppressWarnings("SameParameterValue") final Book book) {
        return Book.builder()
                .name(book.getName())
                .build();
    }
}
