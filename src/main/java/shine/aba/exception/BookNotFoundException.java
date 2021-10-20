package shine.aba.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException() {
        super("Could not find address book");
    }

    public BookNotFoundException(Long id) {
        super("Could not find address book: " + id);
    }
}
