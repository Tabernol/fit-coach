package com.krasnopolskyi.rest;

import com.krasnopolskyi.fitcoach.dto.request.TraineeDto;
import com.krasnopolskyi.fitcoach.dto.request.TrainerDto;
import com.krasnopolskyi.fitcoach.dto.request.UserCredentials;
import com.krasnopolskyi.fitcoach.dto.response.TraineeProfileDto;
import com.krasnopolskyi.IntegrationTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

public class FreeEndPointsTestIT extends IntegrationTestBase {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void createTraineeIT() {
        // Arrange
        TraineeDto traineeDto = TraineeDto.builder().firstName("Maks").lastName("Maks").build();
        ResponseEntity<UserCredentials> responseEntity = this.restTemplate
                .postForEntity("http://localhost:" + port + "/api/v1/trainees/create", traineeDto, UserCredentials.class);
        assertEquals("maks.maks", responseEntity.getBody().username());
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    void createTrainerIT() {
        // Arrange
        TrainerDto trainerDto = TrainerDto.builder().firstName("Coach").lastName("Coach").specialization(1).build();
        ResponseEntity<UserCredentials> responseEntity = this.restTemplate
                .postForEntity("http://localhost:" + port + "/api/v1/trainers/create", trainerDto, UserCredentials.class);
        assertEquals("coach.coach", responseEntity.getBody().username());
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    void loginIT(){
        UserCredentials credentials = new UserCredentials("john.doe", "root");
        ResponseEntity<String> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/v1/authn/login", credentials, String.class);
        assertNotNull(response.getBody());
    }

//    @Test
//    void notAuthenticateRequest(){
//        String username = "john.doe";
//
//        // Use exchange method instead of getForEntity to handle the generic type
//        ResponseEntity<TraineeProfileDto> response = restTemplate.exchange(
//                "http://localhost:" + port + "/api/v1/trainees/{username}",
//                HttpMethod.GET,
//                null,
//                TraineeProfileDto.class,
//                username);
//
//        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
//    }
}
