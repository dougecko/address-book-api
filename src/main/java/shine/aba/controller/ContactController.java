package shine.aba.controller;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import shine.aba.exception.InvalidPhoneNumberException;
import shine.aba.model.Contact;
import shine.aba.model.UniqueContactsRequest;
import shine.aba.service.ContactService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping("contacts")
    Contact addContact(@Valid @RequestBody Contact contact) {
        final String rawPhoneNumber = contact.getPhone();

        final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        if (!phoneNumberUtil.isPossibleNumber(rawPhoneNumber, "AU")) {
            log.error("Invalid phone number \"" + rawPhoneNumber + "\"");
            throw new InvalidPhoneNumberException(rawPhoneNumber, "is invalid for Australia");
        }
        return contactService.addContact(contact);
    }

    @GetMapping("/contacts/{contactId}")
    Contact getContact(@PathVariable Long contactId) {
        return contactService.findContact(contactId);
    }

    @GetMapping("/contacts")
    List<Contact> getContacts() {
        return contactService.findAllContacts();
    }

    @DeleteMapping("/contacts/{contactId}")
    void removeContact(@PathVariable Long contactId) {
        contactService.removeContact(contactId);
    }

    @PostMapping("/uniquecontacts")
    List<Contact> getUniqueContacts(@RequestBody UniqueContactsRequest request) {
        log.debug("getUniqueContacts with " + request.toString());
        return contactService.findUniqueContactsInBooks(request.getBookIds());
    }
}
