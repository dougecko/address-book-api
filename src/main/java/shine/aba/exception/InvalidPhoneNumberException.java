package shine.aba.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidPhoneNumberException extends RuntimeException {

    public InvalidPhoneNumberException(String phone, String messageSuffix) {
        super("Phone number \"" + phone + "\" " + messageSuffix);
    }
}
