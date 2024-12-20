package com.krasnopolskyi.fitcoach.http.rest;

import com.krasnopolskyi.fitcoach.dto.request.*;
import com.krasnopolskyi.fitcoach.dto.response.TraineeProfileDto;
import com.krasnopolskyi.fitcoach.dto.response.TrainerProfileShortDto;
import com.krasnopolskyi.fitcoach.dto.response.TrainingResponseDto;
import com.krasnopolskyi.fitcoach.exception.EntityException;
import com.krasnopolskyi.fitcoach.exception.ValidateException;
import com.krasnopolskyi.fitcoach.http.metric.TrackCountMetric;
import com.krasnopolskyi.fitcoach.service.TraineeService;
import com.krasnopolskyi.fitcoach.validation.Create;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/trainees")
@RequiredArgsConstructor
@Slf4j
public class TraineeController {
    private final TraineeService traineeService;

    /**
     * Provides public end-point for creating trainee
     *
     * @param traineeDto dto with user fields
     * @return credentials for authentication generated username and password
     */
    @Operation(summary = "Create a new trainee",
            description = "Creates a new trainee and returns the generated username and password for authentication.")
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @TrackCountMetric(name = "api_trainee_create",
            description = "Number of requests to /api/v1/trainees/public endpoint")
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
    @Operation(summary = "Get trainee profile by username", description = "Fetches the profile information of a trainee based on the provided username.")

    @PreAuthorize("hasAuthority('TRAINEE')")
    @GetMapping("/{username}")
    public ResponseEntity<TraineeProfileDto> getTrainee(@PathVariable("username") String username) throws EntityException {
        return ResponseEntity.status(HttpStatus.OK).body(traineeService.findByUsername(username));
    }

    /**
     * Provides end-point for retrieve data about all trainers with whom current trainee did not have training session
     *
     * @param username is unique name of trainee
     * @return List of trainers in short format
     * @throws EntityException will be throw if trainee does not exist with such username, but trainer profile can exist
     */
    @Operation(summary = "Get trainers not assigned to trainee",
            description = "Retrieves all trainers who have not yet had a training session with the specified trainee.")
    @PreAuthorize("hasAuthority('TRAINEE')")
    @GetMapping("/{username}/trainers/not-assigned")
    public ResponseEntity<List<TrainerProfileShortDto>> getAllActiveTrainersForTrainee(
            @PathVariable("username") String username) throws EntityException {
        return ResponseEntity.status(HttpStatus.OK).body(traineeService.findAllNotAssignedTrainersByTrainee(username));
    }

    /**
     * provides filtering functionality for training sessions of trainee
     *
     * @param username     target trainee for searching
     * @param periodFrom   date from
     * @param periodTo     date to
     * @param partner      trainer
     * @param trainingType type of training
     * @return List of trainings otherwise empty list
     * @throws EntityException will be thrown if target username does not exist as trainee
     */
    @Operation(summary = "Filter trainee's training sessions",
            description = "Provides filtering functionality for the training sessions of a trainee.")
    @PreAuthorize("hasAuthority('TRAINEE')")
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
     *
     * @param traineeDto Dto
     * @return Dto with other fields
     * @throws EntityException will be throw if trainee does not exist with such username
     */
    @Operation(summary = "Update trainee profile",
            description = "Updates the trainee profile with the provided details.")
    @PreAuthorize("hasAuthority('TRAINEE')")
    @PutMapping("/{username}")
    public ResponseEntity<TraineeProfileDto> updateTrainee(
            @PathVariable("username") String username,
            @Validated(Create.class) @RequestBody TraineeUpdateDto traineeDto)
            throws EntityException, ValidateException {
        return ResponseEntity.status(HttpStatus.OK).body(traineeService.update(username, traineeDto));
    }

    /**
     * Update list of trainers with whom trainee has training session
     *
     * @param username         of target trainee
     * @param trainerUsernames List with usernames of trainers
     * @return List of trainers
     * @throws EntityException will be thrown if username of trainee or trainer does not exist
     */
    @Operation(summary = "Update trainee trainers",
            description = "Updates the list of trainers with whom the trainee has had training sessions.")
    @PreAuthorize("hasAuthority('TRAINEE')")
    @PutMapping("/{username}/trainers/update")
    public ResponseEntity<List<TrainerProfileShortDto>> updateTrainers(
            @PathVariable String username,
            @RequestBody List<String> trainerUsernames) throws EntityException {
        return ResponseEntity.status(HttpStatus.OK).body(traineeService.updateTrainers(username, trainerUsernames));
    }

    /**
     * Provides functionality for changing trainee status
     *
     * @param username  of target trainee
     * @param statusDto dto with username and status
     * @return message of result this action
     * @throws EntityException   if username does not exist
     * @throws ValidateException if username in pathVariable and in body are different
     */
    @Operation(summary = "Toggle trainee status",
            description = "Changes the status (active/inactive) of the trainee.")
    @PreAuthorize("hasAuthority('TRAINEE')")
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
    @Operation(summary = "Delete trainee",
            description = "Deletes the trainee and all associated data in Cascade.ALL mode.")
    @PreAuthorize("hasAuthority('TRAINEE')")
    @DeleteMapping("/{username}")
    public ResponseEntity<?> deleteTrainee(@PathVariable("username") String username) {
        return traineeService.delete(username) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
