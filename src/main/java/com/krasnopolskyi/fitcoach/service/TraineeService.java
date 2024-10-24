package com.krasnopolskyi.fitcoach.service;


import com.krasnopolskyi.fitcoach.dto.request.TraineeDto;
import com.krasnopolskyi.fitcoach.dto.request.UserCredentials;
import com.krasnopolskyi.fitcoach.dto.request.UserDto;
import com.krasnopolskyi.fitcoach.dto.response.TraineeProfileDto;
import com.krasnopolskyi.fitcoach.dto.response.TrainerProfileShortDto;
import com.krasnopolskyi.fitcoach.entity.Trainee;
import com.krasnopolskyi.fitcoach.entity.Trainer;
import com.krasnopolskyi.fitcoach.entity.User;
import com.krasnopolskyi.fitcoach.exception.EntityException;
import com.krasnopolskyi.fitcoach.repository.TraineeRepository;
import com.krasnopolskyi.fitcoach.repository.TrainerRepository;
import com.krasnopolskyi.fitcoach.utils.mapper.TraineeMapper;
import com.krasnopolskyi.fitcoach.utils.mapper.TrainerMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TraineeService {
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final UserService userService;

    @Transactional
    public UserCredentials save(TraineeDto traineeDto) {
        User newUser = userService
                .create(new UserDto(traineeDto.getFirstName(),
                        traineeDto.getLastName())); //return user with firstName, lastName, username, password, isActive

        Trainee trainee = TraineeMapper.mapToEntity(traineeDto, newUser);

        Trainee savedTrainee = traineeRepository.save(trainee);// pass to repository
        log.debug("trainee has been saved " + trainee);
        return new UserCredentials(savedTrainee.getUser().getUsername(), savedTrainee.getUser().getPassword());
    }

//    @Transactional(readOnly = true)
//    public TraineeResponseDto findById(Long id) throws EntityException {
//        return TraineeMapper.mapToDto(findTraineeById(id));
//
//    }
//
    @Transactional(readOnly = true) //generate test
    public TraineeProfileDto findByUsername(String username) throws EntityException {
        return traineeRepository.findByUsername(username)
                .map(trainee -> TraineeMapper.mapToDto(trainee))
                .orElseThrow(() -> new EntityException("Can't find trainee with username " + username));
    }
//
//    @Transactional
//    public TraineeResponseDto update(TraineeDto traineeDto) throws EntityException {
//        // find trainee entity
//        Trainee trainee = findTraineeById(traineeDto.getId());
//        //update trainee's fields
//        trainee.setAddress(traineeDto.getAddress());
//        trainee.setDateOfBirth(traineeDto.getDateOfBirth());
//
//        //update user's fields
//        User user = userService.findById(trainee.getUser().getId()); // find user associated with trainee
//        user.setFirstName(traineeDto.getFirstName());
//        user.setLastName(traineeDto.getLastName());
//
//        Trainee savedTrainee = traineeRepository.save(trainee);
//        log.debug("trainee has been updated " + trainee.getId());
//        return TraineeMapper.mapToDto(savedTrainee);
//    }
//
    @Transactional
    public boolean delete(String username) throws EntityException {
        return traineeRepository.findByUsername(username)
                .map(entity -> {
                    traineeRepository.delete(entity);
                    traineeRepository.flush();
                    return true;
                })
                .orElse(false);
    }

    @Transactional
    public List<TrainerProfileShortDto> updateTrainers(String username, List<String> trainerUsernames) throws EntityException {
        Trainee trainee = traineeRepository.findByUsername(username)
                .orElseThrow(() -> new EntityException("Can't find trainee with username " + username));

        trainee.getTrainers().clear();
        for (String trainerUsername : trainerUsernames) {
            Trainer trainer = trainerRepository.findByUsername(trainerUsername)
                    .orElseThrow(() -> new EntityException("Could not found trainer with id " + trainerUsername));
            trainer.getTrainees().add(trainee);
            trainee.getTrainers().add(trainer);
        }

        return trainee.getTrainers()
                .stream()
                .map(TrainerMapper::mapToShortDto)
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public List<TrainerProfileShortDto> findAllNotAssignedTrainersByTrainee(String username) throws EntityException {
        Trainee trainee = traineeRepository.findByUsername(username)
                .orElseThrow(() -> new EntityException("Can't find trainee with username " + username));

        List<Trainer> allTrainers = trainerRepository.findAllActiveTrainers();
        allTrainers.removeAll(trainee.getTrainers());
        return allTrainers.stream().map(TrainerMapper::mapToShortDto).toList();
    }
//
//
//    private Trainee findTraineeById(Long id) throws EntityException {
//        return traineeRepository.findById(id)
//                .orElseThrow(() -> new EntityException("Could not found trainee with id " + id)); // find trainee entity
//    }

}
