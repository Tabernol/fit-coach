package com.krasnopolskyi.fitcoach.http.rest;

import com.krasnopolskyi.fitcoach.dto.training.TrainingDto;
import com.krasnopolskyi.fitcoach.exception.UserNotFoundException;
import com.krasnopolskyi.fitcoach.service.TrainingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/trainings")
public class TrainingController {

    private final TrainingService trainingService;

    public TrainingController(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @PostMapping
    public ResponseEntity<Boolean> addTraining(@RequestBody TrainingDto trainingDto) throws UserNotFoundException {
        return ResponseEntity.status(HttpStatus.CREATED).body(trainingService.addTraining(trainingDto));
    }
}
