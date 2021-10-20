package shine.aba.service;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import shine.aba.exception.BookAlreadyExistsException;
import shine.aba.exception.BookNotFoundException;
import shine.aba.model.Book;
import shine.aba.model.Contact;
import shine.aba.repository.BookRepository;

import java.util.ArrayList;
import java.util.List;

@Component
public class BookService {

    private final BookRepository bookRepository;

    public BookService(final BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book addBook(final Book book) {
        final Long id = book.getId();
        if (null != id && bookRepository.findById(id).isPresent()) {
            throw new BookAlreadyExistsException(id);
        }
        return bookRepository.save(book);
    }

    public Book updateBook(final Book book) {
        final Long id = book.getId();
        if (null == id) {
            throw new BookNotFoundException();
        }
        // verify book exists
        bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        // save passed book with updated details
        return bookRepository.save(book);
    }

    public void removeBook(final Long id) {
        try {
            bookRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            // convert exception so ResponseStatus is used
            throw new BookNotFoundException(id);
        }
    }

    public Book findBook(final Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    public List<Contact> findContactsInBook(final Long bookId) {
        return new ArrayList<>(findBook(bookId).getContacts());
    }
}
