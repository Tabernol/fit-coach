package com.krasnopolskyi.fitcoach.service.impl;

import com.krasnopolskyi.fitcoach.dto.RegistrationResponse;
import com.krasnopolskyi.fitcoach.dto.coach.CoachCreateRequest;
import com.krasnopolskyi.fitcoach.dto.coach.CoachDto;
import com.krasnopolskyi.fitcoach.dto.exception.UserNotFoundException;
import com.krasnopolskyi.fitcoach.entity.Coach;
import com.krasnopolskyi.fitcoach.entity.TrainingType;
import com.krasnopolskyi.fitcoach.entity.User;
import com.krasnopolskyi.fitcoach.repository.CoachRepository;
import com.krasnopolskyi.fitcoach.service.CoachService;
import com.krasnopolskyi.fitcoach.service.TrainingTypeService;
import com.krasnopolskyi.fitcoach.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CoachServiceImpl implements CoachService {
    private final CoachRepository repository;
    private final UserService userService;

    private final TrainingTypeService trainingTypeService;

    public CoachServiceImpl(
            CoachRepository repository,
            UserService userService,
            TrainingTypeService trainingTypeService) {
        this.repository = repository;
        this.userService = userService;
        this.trainingTypeService = trainingTypeService;
    }

    private Coach findByUsername(String username) throws UserNotFoundException {
        return repository.findByUsername(username).orElseThrow(
                () -> new UserNotFoundException("Not found user with username " + username));
    }

    @Override
    public CoachDto get(String username) throws UserNotFoundException {
        Coach coach = findByUsername(username);
        return mapToDto(coach);
    }

    @Override
    public RegistrationResponse create(CoachCreateRequest request) {
        // save user
        User user = userService.create(request.firstName(), request.lastName());

        Coach coach = new Coach();
        coach.setUser(user);
        TrainingType trainingType = trainingTypeService.findByName(request.specialization());
        coach.setSpecialization(trainingType);

        repository.save(coach);
        return new RegistrationResponse(user.getUsername(), user.getPassword());
    }

    @Override
    public CoachDto update(CoachDto coachDto) throws UserNotFoundException {
        Coach coach = findByUsername(coachDto.getUsername());
        User user = coach.getUser();
        user.setFirstName(coachDto.getFirstName());
        user.setLastName(coachDto.getLastName());
        user.setActive(coachDto.isActive());
        coach.setUser(user);
        return null;
    }

    @Override
    @Transactional
    public boolean delete(String username) {
        return userService.delete(username);
    }

    private CoachDto mapToDto(Coach coach){
        return CoachDto.builder()
                .firstName(coach.getUser().getFirstName())
                .lastName(coach.getUser().getLastName())
                .username(coach.getUser().getUsername())
                .isActive(coach.getUser().isActive())
                .specialization(coach.getSpecialization().getTrainingTypeName())
                // todo add trainees
                .build();
    }
}
