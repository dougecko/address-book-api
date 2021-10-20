package shine.aba.controller;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import shine.aba.exception.InvalidPhoneNumberException;
import shine.aba.model.Contact;
import shine.aba.service.ContactService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
public class AddressBookController {

    private final ContactService contactService;

    public AddressBookController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping("/contacts")
    Contact addContact(@Valid @RequestBody Contact contact) {
        final String rawPhoneNumber = contact.getPhone();

        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
//        final Phonenumber.PhoneNumber phoneNumber;
//        try {
//            phoneNumber = phoneNumberUtil.parse(rawPhoneNumber, Phonenumber.PhoneNumber.CountryCodeSource.UNSPECIFIED.name());
//        } catch (NumberParseException e) {
//            log.error("Unable to parse phone number \"" + rawPhoneNumber + "\"", e);
//            throw new InvalidPhoneNumberException(rawPhoneNumber, "could not be parsed");
//        }
        if (!phoneNumberUtil.isPossibleNumber(rawPhoneNumber, "AU")) {
            log.error("Invalid phone number \"" + rawPhoneNumber + "\"");
            throw new InvalidPhoneNumberException(rawPhoneNumber, "is invalid for Australia");
        }
        return contactService.addContact(contact);
    }

    @GetMapping("/contacts/{id}")
    Contact getContact(@PathVariable Long id) {
        return contactService.findContact(id);
    }

    @GetMapping("/contacts")
    List<Contact> getContacts() {
        return contactService.findAllContacts();
    }

    @DeleteMapping("/contacts/{id}")
    void removeContact(@PathVariable Long id) {
        contactService.removeContact(id);
    }
}
