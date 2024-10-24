package com.krasnopolskyi.fitcoach.http.rest;

import com.krasnopolskyi.fitcoach.dto.request.TraineeDto;
import com.krasnopolskyi.fitcoach.dto.request.UserCredentials;
import com.krasnopolskyi.fitcoach.dto.response.TraineeProfileDto;
import com.krasnopolskyi.fitcoach.dto.response.TrainerProfileShortDto;
import com.krasnopolskyi.fitcoach.exception.EntityException;
import com.krasnopolskyi.fitcoach.service.TraineeService;
import com.krasnopolskyi.fitcoach.validation.Create;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/trainees")
@RequiredArgsConstructor
public class TraineeController {

    private final TraineeService traineeService;


    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserCredentials> createTrainee(
            @Validated(Create.class) @RequestBody TraineeDto traineeDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(traineeService.save(traineeDto));
    }

    @GetMapping("/{username}")
    public ResponseEntity<TraineeProfileDto> getTrainee(@PathVariable("username") String username) throws EntityException {
        return ResponseEntity.status(HttpStatus.OK).body(traineeService.findByUsername(username));
    }


    @GetMapping("/{username}/not-assigned-trainers")
    public ResponseEntity<List<TrainerProfileShortDto>> getAllActiveTrainersForTrainee(
            @PathVariable("username") String username) throws EntityException {
        return ResponseEntity.status(HttpStatus.OK).body(traineeService.findAllNotAssignedTrainersByTrainee(username));
    }

    //
//    @PutMapping()
//    public ResponseEntity<TraineeDto> updateTrainee(@RequestBody TraineeDto traineeDto)
//            throws UserNotFoundException {
//        return ResponseEntity.status(HttpStatus.OK).body(traineeService.update(traineeDto));
//    }

    @PutMapping("/{username}/update-trainers")
    public ResponseEntity<List<TrainerProfileShortDto>> updateTrainers(
            @PathVariable String username,
            @RequestBody List<String> trainerUsernames) throws EntityException {
        return ResponseEntity.status(HttpStatus.OK).body(traineeService.updateTrainers(username, trainerUsernames));
    }


    @DeleteMapping("/{username}")
    public ResponseEntity<?> deleteTrainee(@PathVariable("username") String username) throws EntityException {
        return traineeService.delete(username) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
