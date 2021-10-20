package shine.aba.service;

import org.springframework.stereotype.Component;
import shine.aba.exception.ContactNotFoundException;
import shine.aba.model.Contact;
import shine.aba.repository.ContactRepository;

import java.util.List;

@Component
public class ContactService {

    private final ContactRepository contactRepository;

    public ContactService(final ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public Contact addContact(final Contact contact) {
        // TODO: validate contact does not exist
        return contactRepository.save(contact);
    }

    public Contact updateContact(final Contact contact) {
        final Long id = contact.getId();
        if (null == id) {
            throw new ContactNotFoundException();
        }
        // verify contact exists, but save passed contact with updated details
        contactRepository.findById(id)
                .orElseThrow(() -> new ContactNotFoundException(id));
        return contactRepository.save(contact);
    }

    public void removeContact(final Long id) {
        contactRepository.deleteById(id);
    }

    public Contact findContact(final Long id) {
        return contactRepository.findById(id)
                .orElseThrow(() -> new ContactNotFoundException(id));
    }

    public List<Contact> findAllContacts() {
        return contactRepository.findAll();
    }
}
