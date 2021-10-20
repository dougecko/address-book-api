package shine.aba.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import shine.aba.model.Book;
import shine.aba.model.Contact;
import shine.aba.service.BookService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("books")
    Book addBook(@Valid @RequestBody Book book) {
        return bookService.addBook(book);
    }

    @GetMapping("/books/{bookId}")
    Book getBook(@PathVariable Long bookId) {
        return bookService.findBook(bookId);
    }

    @GetMapping("/books/{bookId}/contacts")
    List<Contact> getContactsInBook(@PathVariable Long bookId) {
        return bookService.findContactsInBook(bookId);
    }

    @GetMapping("/books")
    List<Book> getBooks() {
        return bookService.findAllBooks();
    }

    @DeleteMapping("/books/{bookId}")
    void removeContact(@PathVariable Long bookId) {
        bookService.removeBook(bookId);
    }
}
