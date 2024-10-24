package com.krasnopolskyi.fitcoach.service;

import com.krasnopolskyi.fitcoach.dto.request.TrainerDto;
import com.krasnopolskyi.fitcoach.dto.request.UserCredentials;
import com.krasnopolskyi.fitcoach.dto.request.UserDto;
import com.krasnopolskyi.fitcoach.dto.response.TrainerProfileDto;
import com.krasnopolskyi.fitcoach.entity.Trainer;
import com.krasnopolskyi.fitcoach.entity.TrainingType;
import com.krasnopolskyi.fitcoach.entity.User;
import com.krasnopolskyi.fitcoach.exception.EntityException;
import com.krasnopolskyi.fitcoach.exception.ValidateException;
import com.krasnopolskyi.fitcoach.repository.TrainerRepository;
import com.krasnopolskyi.fitcoach.utils.mapper.TrainerMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
@RequiredArgsConstructor
public class TrainerService {

    private final TrainerRepository trainerRepository;
    private final UserService userService;
    private final TrainingTypeService trainingTypeService;

    @Transactional
    public UserCredentials save(TrainerDto trainerDto) throws EntityException {
//        validate(trainerDto); // validate specialization
        TrainingType specialization = trainingTypeService.findById(trainerDto.getSpecialization()); // receive specialization

        User newUser = userService
                .create(new UserDto(trainerDto.getFirstName(),
                        trainerDto.getLastName())); //return user with firstName, lastName, username, password, isActive
        Trainer trainer = new Trainer();
        trainer.setUser(newUser);
        trainer.setSpecialization(specialization);

        Trainer saveTrainer = trainerRepository.save(trainer);// save entity
        return new UserCredentials(saveTrainer.getUser().getUsername(), saveTrainer.getUser().getPassword());
    }

//    @Transactional(readOnly = true)
//    public TrainerResponseDto findById(Long id) throws EntityException {
//        Trainer trainer = trainerRepository.findById(id)
//                .orElseThrow(() -> new EntityException("Could not found trainer with id " + id));
//        return TrainerMapper.mapToDto(trainer);
//    }
//
    @Transactional(readOnly = true)
    public TrainerProfileDto findByUsername(String username) throws EntityException {
        return trainerRepository.findByUsername(username)
                .map(trainer -> TrainerMapper.mapToDto(trainer))
                .orElseThrow(() -> new EntityException("Can't find trainer with username " + username));
    }
//
//
//    @Transactional
//    public TrainerResponseDto update(TrainerDto trainerDto) throws GymException {
//        Trainer trainer = trainerRepository.findById(trainerDto.getId())
//                .orElseThrow(() -> new EntityException("Could not found trainer with id " + trainerDto.getId()));
//        //update user's fields
//        User user = userService.findById(trainer.getUser().getId()); // get user from repository
//        user.setFirstName(trainerDto.getFirstName()); // here I change user's field and don't save them to trainer explicitly
//        user.setLastName(trainerDto.getLastName()); // but them also will be safe because this user exist in the same transaction
//
//        //update trainer's fields
//        if(trainerDto.getSpecialization() != null){
//            TrainingType specialization = trainingTypeService.findById(trainerDto.getSpecialization());
//            trainer.setSpecialization(specialization);
//        }
//        Trainer savedTrainer = trainerRepository.save(trainer); // pass refreshed trainer to repository
//        return TrainerMapper.mapToDto(savedTrainer);
//    }
//
//
    private void validate(TrainerDto trainerDto) throws ValidateException {
        try {
            trainingTypeService.findById(trainerDto.getSpecialization());
        } catch (EntityException e) {
            log.warn("Attempt to save trainer with does not exist specialization " + trainerDto.getSpecialization());
            throw new ValidateException("Specialisation with id " + trainerDto.getSpecialization() + " does not exist");
        }
    }
}
