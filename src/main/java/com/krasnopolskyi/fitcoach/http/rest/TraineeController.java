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

    @PutMapping()
    public ResponseEntity<TraineeProfileDto> updateTrainee(
            @Validated(Create.class) @RequestBody TraineeUpdateDto traineeDto) throws EntityException {
        return ResponseEntity.status(HttpStatus.OK).body(traineeService.update(traineeDto));
    }

    @PutMapping("/{username}/update-trainers")
    public ResponseEntity<List<TrainerProfileShortDto>> updateTrainers(
            @PathVariable String username,
            @RequestBody List<String> trainerUsernames) throws EntityException {
        return ResponseEntity.status(HttpStatus.OK).body(traineeService.updateTrainers(username, trainerUsernames));
    }
    @PatchMapping("/{username}/toggle-status")
    public ResponseEntity<String> toggleStatus(
            @PathVariable("username") String username,
            @RequestBody ToggleStatusDto statusDto) throws EntityException, ValidateException {
        return ResponseEntity.status(HttpStatus.OK).body(traineeService.changeStatus(username, statusDto));
    }


    @DeleteMapping("/{username}")
    public ResponseEntity<?> deleteTrainee(@PathVariable("username") String username) throws EntityException {
        return traineeService.delete(username) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
