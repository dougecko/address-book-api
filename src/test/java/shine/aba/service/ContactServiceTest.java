package shine.aba.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import shine.aba.exception.ContactNotFoundException;
import shine.aba.model.Contact;
import shine.aba.repository.ContactRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class ContactServiceTest {

    private static final long CONTACT_ID = 1;
    private static final Contact EXISTING_CONTACT;
    private static final Contact NEW_CONTACT;

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
    }

    @Mock
    private ContactRepository repository;

    @InjectMocks
    private ContactService service;

    @Test
    void updateContact_whenContactExists_succeeds() {
        when(repository.findById(CONTACT_ID)).thenReturn(Optional.of(EXISTING_CONTACT));

        when(repository.save(any())).thenReturn(NEW_CONTACT);

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

    private static Contact cloneWithoutId(@SuppressWarnings("SameParameterValue") final Contact contact) {
        return Contact.builder()
                .name(contact.getName())
                .phone(contact.getPhone())
                .build();
    }
}
