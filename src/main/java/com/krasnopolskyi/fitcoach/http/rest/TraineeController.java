package com.krasnopolskyi.fitcoach.http.rest;

import com.krasnopolskyi.fitcoach.dto.request.*;
import com.krasnopolskyi.fitcoach.dto.response.TraineeProfileDto;
import com.krasnopolskyi.fitcoach.dto.response.TrainerProfileShortDto;
import com.krasnopolskyi.fitcoach.dto.response.TrainingResponseDto;
import com.krasnopolskyi.fitcoach.exception.EntityException;
import com.krasnopolskyi.fitcoach.exception.ValidateException;
import com.krasnopolskyi.fitcoach.service.TraineeService;
import com.krasnopolskyi.fitcoach.validation.Create;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/trainees")
@RequiredArgsConstructor
public class TraineeController {
    private final TraineeService traineeService;

    /**
     * Provides public end-point for creating trainee
     * @param traineeDto dto with user fields
     * @return credentials for authentication generated username and password
     */
    @PostMapping("/public")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserCredentials> createTrainee(
            @Validated(Create.class) @RequestBody TraineeDto traineeDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(traineeService.save(traineeDto));
    }

    /**
     * Provides end-point for retrieve data about trainee
     * @param username is unique name of trainee
     * @return dto with data about trainee
     * @throws EntityException will be throw if trainee does not exist with such username
     */
    @GetMapping("/{username}")
    public ResponseEntity<TraineeProfileDto> getTrainee(@PathVariable("username") String username) throws EntityException {
        return ResponseEntity.status(HttpStatus.OK).body(traineeService.findByUsername(username));
    }

    /**
     * Provides end-point for retrieve data about all trainers with whom current trainee did not have training session
     * @param username is unique name of trainee
     * @return List of trainers in short format
     * @throws EntityException will be throw if trainee does not exist with such username, but trainer profile can exist
     */
    @GetMapping("/{username}/not-assigned-trainers")
    public ResponseEntity<List<TrainerProfileShortDto>> getAllActiveTrainersForTrainee(
            @PathVariable("username") String username) throws EntityException {
        return ResponseEntity.status(HttpStatus.OK).body(traineeService.findAllNotAssignedTrainersByTrainee(username));
    }

    /**
     * provides filtering functionality for training sessions of trainee
     * @param username target trainee for searching
     * @param periodFrom date from
     * @param periodTo date to
     * @param partner trainer
     * @param trainingType type of training
     * @return List of trainings otherwise empty list
     * @throws EntityException will be thrown if target username does not exist as trainee
     */
    @GetMapping("/{username}/trainings")
    public ResponseEntity<List<TrainingResponseDto>> findTraining(
            @PathVariable String username,
            @RequestParam(required = false) LocalDate periodFrom,
            @RequestParam(required = false) LocalDate periodTo,
            @RequestParam(required = false) String partner,
            @RequestParam(required = false) String trainingType) throws EntityException {

        TrainingFilterDto filter = TrainingFilterDto.builder()
                .owner(username)
                .startDate(periodFrom)
                .endDate(periodTo)
                .partner(partner)
                .trainingType(trainingType)
                .build();

        List<TrainingResponseDto> trainings = traineeService.getTrainings(filter);
        return ResponseEntity.status(HttpStatus.OK).body(trainings);
    }

    /**
     * Update trainee fields
     * @param traineeDto Dto
     * @return Dto with other fields
     * @throws EntityException will be throw if trainee does not exist with such username
     */
    @PutMapping()
    public ResponseEntity<TraineeProfileDto> updateTrainee(
            @Validated(Create.class) @RequestBody TraineeUpdateDto traineeDto) throws EntityException {
        return ResponseEntity.status(HttpStatus.OK).body(traineeService.update(traineeDto));
    }

    /**
     * Update list of trainers with whom trainee has training session
     * @param username of target trainee
     * @param trainerUsernames List with usernames of trainers
     * @return List of trainers
     * @throws EntityException will be thrown if username of trainee or trainer does not exist
     */
    @PutMapping("/{username}/update-trainers")
    public ResponseEntity<List<TrainerProfileShortDto>> updateTrainers(
            @PathVariable String username,
            @RequestBody List<String> trainerUsernames) throws EntityException {
        return ResponseEntity.status(HttpStatus.OK).body(traineeService.updateTrainers(username, trainerUsernames));
    }

    /**
     * Provides functionality for changing trainee status
     * @param username of target trainee
     * @param statusDto dto with username and status
     * @return message of result this action
     * @throws EntityException if username does not exist
     * @throws ValidateException if username in pathVariable and in body are different
     */
    @PatchMapping("/{username}/toggle-status")
    public ResponseEntity<String> toggleStatus(
            @PathVariable("username") String username,
            @Validated(Create.class) @RequestBody ToggleStatusDto statusDto) throws EntityException, ValidateException {
        return ResponseEntity.status(HttpStatus.OK).body(traineeService.changeStatus(username, statusDto));
    }


    /**
     * Deletes trainee in Cascade.ALL mode
     * @param username of target trainee
     * @return noContent when trainee was deleted or not found if trainee does not exist with such username
     */
    @DeleteMapping("/{username}")
    public ResponseEntity<?> deleteTrainee(@PathVariable("username") String username) {
        return traineeService.delete(username) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
