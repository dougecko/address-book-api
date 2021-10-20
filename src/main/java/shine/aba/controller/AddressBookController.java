package shine.aba.controller;

import org.springframework.web.bind.annotation.*;
import shine.aba.model.Contact;
import shine.aba.service.ContactService;

import java.util.List;

@RestController
public class AddressBookController {

    private final ContactService contactService;

    public AddressBookController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping("/contacts")
    Contact addContact(@RequestBody Contact contact) {
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
