package com.krasnopolskyi.fitcoach.service;


import com.krasnopolskyi.fitcoach.dto.request.TrainingDto;
import com.krasnopolskyi.fitcoach.dto.response.TrainingResponseDto;
import com.krasnopolskyi.fitcoach.entity.Trainee;
import com.krasnopolskyi.fitcoach.entity.Trainer;
import com.krasnopolskyi.fitcoach.entity.Training;
import com.krasnopolskyi.fitcoach.entity.TrainingType;
import com.krasnopolskyi.fitcoach.exception.EntityException;
import com.krasnopolskyi.fitcoach.exception.ValidateException;
import com.krasnopolskyi.fitcoach.repository.*;
import com.krasnopolskyi.fitcoach.utils.mapper.TrainingMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrainingService {
    private final TrainingRepository trainingRepository;
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final TrainingTypeRepository trainingTypeRepository;

    private final UserRepository userRepository;

    @Transactional
    public TrainingResponseDto save(TrainingDto trainingDto) throws EntityException {
        Trainee trainee = traineeRepository.findByUsername(trainingDto.getTraineeUsername())
                .orElseThrow(() -> new EntityException("Could not find trainee with " + trainingDto.getTraineeUsername()));

        Trainer trainer = trainerRepository.findByUsername(trainingDto.getTrainerUsername())
                .orElseThrow(() -> new EntityException("Could not find trainer with id " + trainingDto.getTrainerUsername()));

        trainer.getTrainees().add(trainee); // save into set and table trainer_trainee

        Training training = new Training();
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setTrainingType(trainer.getSpecialization());
        training.setDate(trainingDto.getDate());
        training.setDuration(trainingDto.getDuration());
        training.setTrainingName(trainingDto.getTrainingName());

        trainee.getTrainers().add(trainer);
        trainer.getTrainees().add(trainee);

        trainingRepository.save(training);

        return TrainingMapper.mapToDto(training);
    }
//
//    @Transactional(readOnly = true)
//    public TrainingResponseDto findById(Long id) throws EntityException {
//        Training training = trainingRepository.findById(id)
//                .orElseThrow(() -> new EntityException("Could not found training with id " + id));
//        return TrainingMapper.mapToDto(training);
//    }
//
//    @Transactional(readOnly = true)
//    public List<TrainingResponseDto> getFilteredTrainings(TrainingFilterDto filter) throws EntityException {
//        userRepository.findByUsername(filter.getOwner())
//                .orElseThrow(() -> new EntityException("Could not found user: " + filter.getOwner()));
//
//        List<Training> trainings = trainingRepository.getFilteredTrainings(filter);
//
//        return trainings.stream().map(TrainingMapper::mapToDto).collect(Collectors.toList());
//    }
}
