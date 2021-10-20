package shine.aba.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ContactTest {

    /**
     * Ensure that toString() contains useful values for debugging.
     */
    @Test
    void contactValueTest() {
        final String name = "Eugene Icario";
        final String phone = "1234 5678";
        final Contact contact = Contact.builder()
                .name(name)
                .phone(phone)
                .build();

        final String contactString = contact.toString();
        assertTrue(contactString.contains(name));
        assertTrue(contactString.contains(phone));
    }
}
