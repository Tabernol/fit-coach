package com.krasnopolskyi.fitcoach.service;

import com.krasnopolskyi.fitcoach.dto.RegistrationResponse;
import com.krasnopolskyi.fitcoach.dto.coach.CoachCreateRequest;
import com.krasnopolskyi.fitcoach.dto.coach.CoachDto;
import com.krasnopolskyi.fitcoach.exception.UserNotFoundException;

public interface CoachService {

    CoachDto get(String username) throws UserNotFoundException;
    RegistrationResponse create(CoachCreateRequest request);
    CoachDto update(CoachDto coachDto) throws UserNotFoundException;
    boolean delete(String username);
}
