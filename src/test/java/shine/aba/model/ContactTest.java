package shine.aba.model;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Ensure that toString() contains useful values for debugging.
 */
class ContactTest {

    private static final String NAME = "Eugene Icario";
    private static final String PHONE = "1234 5678";

    @Test
    void toString_withNoContacts() {
        final Contact contact = Contact.builder()
                .name(NAME)
                .phone(PHONE)
                .build();

        final String contactString = contact.toString();
        assertTrue(contactString.contains(NAME));
        assertTrue(contactString.contains(PHONE));
    }

    @Test
    void toString_withContacts() {
        final Contact contact = Contact.builder()
                .name(NAME)
                .phone(PHONE)
                .build();
        contact.setBooks(Set.of(Book.builder().build()));

        final String contactString = contact.toString();
        assertTrue(contactString.contains("books=1"));
    }
}
