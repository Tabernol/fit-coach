package com.krasnopolskyi.fitcoach.service;

import com.krasnopolskyi.fitcoach.dto.training.TrainingDto;
import com.krasnopolskyi.fitcoach.entity.Training;
import com.krasnopolskyi.fitcoach.exception.UserNotFoundException;

import java.util.List;

public interface TrainingService {

    boolean addTraining(TrainingDto trainingDto) throws UserNotFoundException;

    List<Training> getAllByUsername(String username);
}
