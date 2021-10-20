package shine.aba.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AddressBookControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void getAllContacts() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/contacts").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
