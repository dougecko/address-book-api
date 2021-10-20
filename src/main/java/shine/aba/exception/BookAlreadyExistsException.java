package shine.aba.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BookAlreadyExistsException extends RuntimeException {

    public BookAlreadyExistsException(Long id) {
        super("Address book already exists: " + id);
    }
}
