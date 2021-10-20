package shine.aba.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import shine.aba.exception.ContactAlreadyExistsException;
import shine.aba.exception.ContactNotFoundException;
import shine.aba.model.Contact;
import shine.aba.repository.ContactRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class ContactServiceTest {

    private static final long CONTACT_ID = 1;
    private static final long ANOTHER_CONTACT_ID = 2;
    private static final Contact EXISTING_CONTACT;
    private static final Contact NEW_CONTACT;
    private static final Contact ANOTHER_CONTACT;

    static {
        EXISTING_CONTACT = Contact.builder()
                .name("Dr Test")
                .phone("0987 6543")
                .build();
        EXISTING_CONTACT.setId(CONTACT_ID);

        NEW_CONTACT = Contact.builder()
                .name("Just Test is fine")
                .phone("0987 6541")
                .build();
        NEW_CONTACT.setId(CONTACT_ID);

        ANOTHER_CONTACT = Contact.builder()
                .name("Bobby Tables")
                .phone("0812 3456")
                .build();
        EXISTING_CONTACT.setId(ANOTHER_CONTACT_ID);
    }

    @Mock
    private ContactRepository repository;

    @InjectMocks
    private ContactService service;

    @Test
    void addContact_whenContactDoesNotExist_succeeds() {
        when(repository.findById(CONTACT_ID)).thenReturn(Optional.empty());

        when(repository.saveAndFlush(any())).thenReturn(NEW_CONTACT);

        final Contact addedContact = service.addContact(NEW_CONTACT);
        assertEquals(NEW_CONTACT, addedContact);
    }

    @Test
    void addContact_whenContactExists_throwsException() {
        when(repository.findById(CONTACT_ID)).thenReturn(Optional.of(EXISTING_CONTACT));

        assertThrows(ContactAlreadyExistsException.class, () -> service.addContact(NEW_CONTACT));
    }

    @Test
    void updateContact_whenContactExists_succeeds() {
        when(repository.findById(CONTACT_ID)).thenReturn(Optional.of(EXISTING_CONTACT));

        when(repository.saveAndFlush(any())).thenReturn(NEW_CONTACT);

        final Contact updatedContact = service.updateContact(NEW_CONTACT);
        assertEquals(NEW_CONTACT, updatedContact);
    }

    @Test
    void updateContact_whenContactDoesNotExist_throwsException() {
        when(repository.findById(CONTACT_ID)).thenReturn(Optional.empty());

        assertThrows(ContactNotFoundException.class, () -> service.updateContact(NEW_CONTACT));
    }

    @Test
    void updateContact_whenIdIsNull_throwsException() {
        final Contact nullIdContact = cloneWithoutId(NEW_CONTACT);

        assertThrows(ContactNotFoundException.class, () -> service.updateContact(nullIdContact));
    }

    @Test
    void removeContact_whenContactExists_succeeds() {
        // allow call to succeed
        Mockito.doNothing().when(repository).deleteById(CONTACT_ID);

        // no assertion possible on void, but test will fail if any exception thrown
        service.removeContact(CONTACT_ID);
    }

    @Test
    void removeContact_whenContactDoesNotExist_throwsException() {
        Mockito.doThrow(new EmptyResultDataAccessException(1)).when(repository).deleteById(CONTACT_ID);

        assertThrows(ContactNotFoundException.class, () -> service.removeContact(CONTACT_ID));
    }

    @Test
    void findContact_whenContactExists_succeeds() {
        when(repository.findById(CONTACT_ID)).thenReturn(Optional.of(EXISTING_CONTACT));

        final Contact foundContact = service.findContact(CONTACT_ID);
        assertEquals(EXISTING_CONTACT, foundContact);
    }

    @Test
    void findContact_whenContactDoesNotExist_throwsException() {
        when(repository.findById(CONTACT_ID)).thenReturn(Optional.empty());

        assertThrows(ContactNotFoundException.class, () -> service.findContact(CONTACT_ID));
    }

    @Test
    void findAllContacts_whenOneContactExists_succeeds() {
        when(repository.findAll()).thenReturn(Collections.singletonList(EXISTING_CONTACT));

        final List<Contact> foundContacts = service.findAllContacts();
        assertEquals(1, foundContacts.size());
        assertEquals(EXISTING_CONTACT, foundContacts.get(0));
    }

    @Test
    void findAllContacts_whenTwoContactsExist_succeeds() {
        when(repository.findAll()).thenReturn(List.of(EXISTING_CONTACT, ANOTHER_CONTACT));

        final List<Contact> foundContacts = service.findAllContacts();
        assertEquals(2, foundContacts.size());
        assertEquals(EXISTING_CONTACT, foundContacts.get(0));
        assertEquals(ANOTHER_CONTACT, foundContacts.get(1));
    }

    @Test
    void findAllContacts_whenNoContactsExist_succeeds() {
        when(repository.findAll()).thenReturn(new ArrayList<>());

        final List<Contact> foundContacts = service.findAllContacts();
        assertEquals(0, foundContacts.size());
    }

    private static Contact cloneWithoutId(@SuppressWarnings("SameParameterValue") final Contact contact) {
        return Contact.builder()
                .name(contact.getName())
                .phone(contact.getPhone())
                .build();
    }
}
