package shine.aba.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class BookTest {

    /**
     * Ensure that toString() contains useful values for debugging.
     */
    @Test
    void contactValueTest() {
        final String name = "The Big Book of Names";
        final Book book = Book.builder()
                .name(name)
                .build();

        final String addressBookString = book.toString();
        assertTrue(addressBookString.contains(name));
    }
}
