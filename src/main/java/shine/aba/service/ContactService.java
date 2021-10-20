package shine.aba.service;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import shine.aba.exception.ContactAlreadyExistsException;
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
        final Long id = contact.getId();
        if (null != id && contactRepository.findById(id).isPresent()) {
            throw new ContactAlreadyExistsException(id);
        }
        return contactRepository.save(contact);
    }

    public Contact updateContact(final Contact contact) {
        final Long id = contact.getId();
        if (null == id) {
            throw new ContactNotFoundException();
        }
        // verify contact exists
        contactRepository.findById(id)
                .orElseThrow(() -> new ContactNotFoundException(id));
        // save passed contact with updated details
        return contactRepository.save(contact);
    }

    public void removeContact(final Long id) {
        try {
            contactRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            // convert exception so ResponseStatus is used
            throw new ContactNotFoundException(id);
        }
    }

    public Contact findContact(final Long id) {
        return contactRepository.findById(id)
                .orElseThrow(() -> new ContactNotFoundException(id));
    }

    public List<Contact> findAllContacts() {
        return contactRepository.findAll();
    }
}
