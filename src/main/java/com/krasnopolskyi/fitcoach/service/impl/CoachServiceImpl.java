package com.krasnopolskyi.fitcoach.service.impl;

import com.krasnopolskyi.fitcoach.dto.RegistrationResponse;
import com.krasnopolskyi.fitcoach.dto.coach.CoachCreateRequest;
import com.krasnopolskyi.fitcoach.dto.coach.CoachDto;
import com.krasnopolskyi.fitcoach.dto.exception.UserNotFoundException;
import com.krasnopolskyi.fitcoach.entity.Coach;
import com.krasnopolskyi.fitcoach.entity.User;
import com.krasnopolskyi.fitcoach.repository.CoachRepository;
import com.krasnopolskyi.fitcoach.service.CoachService;
import com.krasnopolskyi.fitcoach.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CoachServiceImpl implements CoachService {
    private final CoachRepository repository;
    private final UserService userService;

    public CoachServiceImpl(CoachRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    @Override
    public CoachDto get(String username) throws UserNotFoundException {
        Coach coach = repository.findByUsername(username).orElseThrow(
                () -> new UserNotFoundException("Not found user with username " + username)
        );
        return CoachDto.builder()
                .firstName(coach.getUser().getFirstName())
                .lastName(coach.getUser().getLastName())
                .isActive(coach.getUser().isActive())
                //todo specialization and list trainees
                .build();
    }

    @Override
    public RegistrationResponse create(CoachCreateRequest request) {
        // save user
        User user = userService.create(request.firstName(), request.lastName());

        Coach coach = new Coach();
        coach.setUser(user);
        coach.setSpecializationId(1); // todo logic with specialization

        repository.save(coach);
        return new RegistrationResponse(user.getUsername(), user.getPassword());
    }

    @Override
    @Transactional
    public boolean delete(String username) {
        return userService.delete(username);
    }
}
