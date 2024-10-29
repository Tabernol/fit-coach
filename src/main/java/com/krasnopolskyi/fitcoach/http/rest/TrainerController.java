package com.krasnopolskyi.fitcoach.http.rest;

import com.krasnopolskyi.fitcoach.dto.request.*;
import com.krasnopolskyi.fitcoach.dto.response.TrainerProfileDto;
import com.krasnopolskyi.fitcoach.dto.response.TrainingResponseDto;
import com.krasnopolskyi.fitcoach.exception.EntityException;
import com.krasnopolskyi.fitcoach.exception.GymException;
import com.krasnopolskyi.fitcoach.exception.ValidateException;
import com.krasnopolskyi.fitcoach.service.TrainerService;
import com.krasnopolskyi.fitcoach.validation.Create;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/trainers")
@RequiredArgsConstructor
public class TrainerController {

    private final TrainerService trainerService;

    @GetMapping("/{username}")
    public ResponseEntity<TrainerProfileDto> getTrainer(@PathVariable("username") String username) throws EntityException {
        return ResponseEntity.status(HttpStatus.OK).body(trainerService.findByUsername(username));
    }

    @GetMapping("/{username}/trainings")
    public ResponseEntity<List<TrainingResponseDto>> findTraining(
            @PathVariable String username,
            @RequestParam(required = false) LocalDate periodFrom,
            @RequestParam(required = false) LocalDate periodTo,
            @RequestParam(required = false) String partner) throws EntityException {

        TrainingFilterDto filter = TrainingFilterDto.builder()
                .owner(username)
                .startDate(periodFrom)
                .endDate(periodTo)
                .partner(partner)
                .build();

        List<TrainingResponseDto> trainings = trainerService.getTrainings(filter);
        return ResponseEntity.status(HttpStatus.OK).body(trainings);
    }

    @PostMapping("/public")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserCredentials> createTrainer(
            @Validated(Create.class) @RequestBody TrainerDto trainerDto) throws EntityException {
        return ResponseEntity.status(HttpStatus.CREATED).body(trainerService.save(trainerDto));
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TrainerProfileDto> updateTrainer(
            @Validated(Create.class) @RequestBody TrainerUpdateDto trainerDto) throws GymException {
        return ResponseEntity.status(HttpStatus.CREATED).body(trainerService.update(trainerDto));
    }

    @PatchMapping("/{username}/toggle-status")
    public ResponseEntity<String> toggleStatus(
            @PathVariable("username") String username,
            @Validated(Create.class) @RequestBody ToggleStatusDto statusDto) throws EntityException, ValidateException {
        return ResponseEntity.status(HttpStatus.OK).body(trainerService.changeStatus(username, statusDto));
    }
}
