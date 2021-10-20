package shine.aba.model;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Ensure that toString() contains useful values for debugging.
 */
class BookTest {

    private static final String NAME = "The Big Book of Names";

    @Test
    void toString_withNoContacts() {
        final Book book = Book.builder()
                .name(NAME)
                .build();

        final String addressBookString = book.toString();
        assertTrue(addressBookString.contains(NAME));
    }

    @Test
    void toString_withContacts() {
        final Book book = Book.builder()
                .name(NAME)
                .build();
        book.setContacts(Set.of(Contact.builder().build()));

        final String addressBookString = book.toString();
        assertTrue(addressBookString.contains("contacts=1"));
    }
}
