package com.krasnopolskyi.fitcoach.service.impl;

import com.krasnopolskyi.fitcoach.dto.RegistrationResponse;
import com.krasnopolskyi.fitcoach.exception.UserNotFoundException;
import com.krasnopolskyi.fitcoach.dto.trainee.TraineeCreateRequest;
import com.krasnopolskyi.fitcoach.dto.trainee.TraineeDto;
import com.krasnopolskyi.fitcoach.entity.Trainee;
import com.krasnopolskyi.fitcoach.entity.User;
import com.krasnopolskyi.fitcoach.repository.TraineeRepository;
import com.krasnopolskyi.fitcoach.service.TraineeService;
import com.krasnopolskyi.fitcoach.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TraineeServiceImpl implements TraineeService {
    private final TraineeRepository repository;
    private final UserService userService;

    public TraineeServiceImpl(TraineeRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    private Trainee findByUsername(String username) throws UserNotFoundException {
        return repository.findByUsername(username).orElseThrow(
                () -> new UserNotFoundException("Not found user with username " + username));
    }

    @Override
    public TraineeDto get(String username) throws UserNotFoundException {
        Trainee trainee = findByUsername(username);
        return mapToDto(trainee);
    }

    @Override
    @Transactional
    public RegistrationResponse create(TraineeCreateRequest request) {
        // save user
        User user = userService.create(request.getFirstName(), request.getLastName());

        //create trainee entity
        Trainee trainee = new Trainee();
        trainee.setUser(user);
        trainee.setAddress(request.getAddress());
        trainee.setDateOfBirth(request.getDateOfBirth());

        //save trainee
        repository.save(trainee);

        return new RegistrationResponse(user.getUsername(), user.getPassword());
    }

    @Override
    public TraineeDto update(TraineeDto traineeDto) throws UserNotFoundException {
        Trainee trainee = findByUsername(traineeDto.getUsername());

        User user = trainee.getUser();
        user.setFirstName(traineeDto.getFirstName());
        user.setLastName(traineeDto.getLastName());
        user.setActive(traineeDto.isActive());

        trainee.setUser(user);
        trainee.setAddress(traineeDto.getAddress());
        trainee.setDateOfBirth(traineeDto.getDateOfBirth());

        return mapToDto(repository.save(trainee));
    }

    @Override
    @Transactional
    public boolean delete(String username) {
        return userService.delete(username);
    }


    private TraineeDto mapToDto(Trainee trainee){
        return TraineeDto.builder()
                .firstName(trainee.getUser().getFirstName())
                .lastName(trainee.getUser().getLastName())
                .username(trainee.getUser().getUsername())
                .dateOfBirth(trainee.getDateOfBirth())
                .address(trainee.getAddress())
                .isActive(trainee.getUser().isActive())
                // todo add coaches
                .build();
    }


}
