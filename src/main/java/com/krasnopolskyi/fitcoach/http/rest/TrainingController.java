package com.krasnopolskyi.fitcoach.http.rest;

import com.krasnopolskyi.fitcoach.dto.request.TrainingDto;
import com.krasnopolskyi.fitcoach.dto.response.TrainingResponseDto;
import com.krasnopolskyi.fitcoach.exception.EntityException;
import com.krasnopolskyi.fitcoach.exception.ValidateException;
import com.krasnopolskyi.fitcoach.service.TrainingService;
import com.krasnopolskyi.fitcoach.validation.Create;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/trainings")
@RequiredArgsConstructor
public class TrainingController {

    private final TrainingService trainingService;


    /**
     * Provides functionality for creating training session
     * @param trainingDto dto with fields
     * @return dto of training session
     * @throws EntityException will be throw if trainer or trainee does not exist
     * @throws ValidateException will be throw if trainee or/and trainer profile deactivated
     */
    @PostMapping
    public ResponseEntity<TrainingResponseDto> addTraining(
            @Validated(Create.class)
            @RequestBody TrainingDto trainingDto)
            throws EntityException, ValidateException {
        return ResponseEntity.status(HttpStatus.CREATED).body(trainingService.save(trainingDto));
    }
}
