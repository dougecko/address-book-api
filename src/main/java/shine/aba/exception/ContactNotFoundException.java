package shine.aba.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ContactNotFoundException extends RuntimeException {

    public ContactNotFoundException() {
        super("Could not find contact");
    }

    public ContactNotFoundException(Long id) {
        super("Could not find contact: " + id);
    }
}
