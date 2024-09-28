package com.krasnopolskyi.fitcoach.http.rest;

import com.krasnopolskyi.fitcoach.dto.RegistrationResponse;
import com.krasnopolskyi.fitcoach.exception.UserNotFoundException;
import com.krasnopolskyi.fitcoach.dto.trainee.TraineeCreateRequest;
import com.krasnopolskyi.fitcoach.dto.trainee.TraineeDto;
import com.krasnopolskyi.fitcoach.service.TraineeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/trainees")
public class TraineeRestController {

    private final TraineeService traineeService;

    public TraineeRestController(TraineeService traineeService) {
        this.traineeService = traineeService;
    }


    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<RegistrationResponse> createTrainee(
            @RequestBody TraineeCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(traineeService.create(request));
    }



    @GetMapping("/{username}")
    public ResponseEntity<TraineeDto> getTrainee(@PathVariable("username")String username)
            throws UserNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(traineeService.get(username));
    }

    @PutMapping()
    public ResponseEntity<TraineeDto> updateTrainee(@RequestBody TraineeDto traineeDto)
            throws UserNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(traineeService.update(traineeDto));
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<?> deleteTrainee(@PathVariable("username")String username) {
        return traineeService.delete(username) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
