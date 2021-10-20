package shine.aba.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ContactAlreadyExistsException extends RuntimeException {

    public ContactAlreadyExistsException(Long id) {
        super("Contact already exists: " + id);
    }
}
