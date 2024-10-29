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


    @PostMapping
    public ResponseEntity<TrainingResponseDto> addTraining(
            @Validated(Create.class)
            @RequestBody TrainingDto trainingDto)
            throws EntityException, ValidateException {
        return ResponseEntity.status(HttpStatus.CREATED).body(trainingService.save(trainingDto));
    }
}
