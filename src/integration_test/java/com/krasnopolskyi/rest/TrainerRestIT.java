package com.krasnopolskyi.rest;

import com.krasnopolskyi.fitcoach.dto.request.UserCredentials;
import com.krasnopolskyi.fitcoach.dto.response.TrainerProfileDto;
import com.krasnopolskyi.IntegrationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TrainerRestIT extends IntegrationTestBase {

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    private String token;

    @BeforeEach
    void setUp() {
        if (token == null) {  // authenticate only if token is not already set
            UserCredentials credentials = new UserCredentials("john.doe", "root");
            ResponseEntity<String> response = restTemplate.postForEntity(
                    "http://localhost:" + port + "/api/v1/authn/login", credentials, String.class);

            token = response.getBody(); // store token
        }

        // Set Authorization header for subsequent requests
        restTemplate.getRestTemplate().getInterceptors().add((request, body, execution) -> {
            request.getHeaders().add("Authorization", "Bearer " + token);
            return execution.execute(request, body);
        });
    }

    @Test
    void getTrainerIT() {
        String username = "usain.bolt";

        // Use exchange method instead of getForEntity to handle the generic type
        ResponseEntity<TrainerProfileDto> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/v1/trainers/{username}",
                HttpMethod.GET,
                null,
                TrainerProfileDto.class,
                username);

        // Assertions (adjust as necessary)
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Usain", response.getBody().firstName());
        assertEquals(username, response.getBody().username());
    }
}
