package integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import shine.aba.Application;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes={Application.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AddressBookControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate template;

    @Test
    public void getBasePath() {
        final String url = "http://localhost:" + port + "/";
        final ResponseEntity<String> response = template.getForEntity(url, String.class);
        assertThat(response.getBody()).isEqualTo("Service returned something");
    }
}