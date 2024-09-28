package com.krasnopolskyi.fitcoach.service;

import com.krasnopolskyi.fitcoach.dto.RegistrationResponse;
import com.krasnopolskyi.fitcoach.dto.exception.UserNotFoundException;
import com.krasnopolskyi.fitcoach.dto.trainee.TraineeCreateRequest;
import com.krasnopolskyi.fitcoach.dto.trainee.TraineeDto;

public interface TraineeService {
    TraineeDto get(String username) throws UserNotFoundException;
    RegistrationResponse create(TraineeCreateRequest request);

    TraineeDto update(TraineeDto traineeDto) throws UserNotFoundException;
    boolean delete(String username);
}
