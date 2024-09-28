package com.krasnopolskyi.fitcoach.service.impl;

import com.krasnopolskyi.fitcoach.dto.training.TrainingDto;
import com.krasnopolskyi.fitcoach.entity.Coach;
import com.krasnopolskyi.fitcoach.entity.Trainee;
import com.krasnopolskyi.fitcoach.entity.Training;
import com.krasnopolskyi.fitcoach.entity.TrainingType;
import com.krasnopolskyi.fitcoach.exception.UserNotFoundException;
import com.krasnopolskyi.fitcoach.repository.CoachRepository;
import com.krasnopolskyi.fitcoach.repository.TraineeRepository;
import com.krasnopolskyi.fitcoach.repository.TrainingRepository;
import com.krasnopolskyi.fitcoach.service.TrainingService;
import com.krasnopolskyi.fitcoach.service.TrainingTypeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingServiceImpl implements TrainingService {
    private final TrainingRepository repository;
    private final TraineeRepository traineeRepository;
    private final CoachRepository coachRepository;
    private final TrainingTypeService trainingTypeService;

    public TrainingServiceImpl(
            TrainingRepository repository,
            TraineeRepository traineeRepository,
            CoachRepository coachRepository,
            TrainingTypeService trainingTypeService) {
        this.repository = repository;
        this.traineeRepository = traineeRepository;
        this.coachRepository = coachRepository;
        this.trainingTypeService = trainingTypeService;
    }


    @Override
    public boolean addTraining(TrainingDto trainingDto) throws UserNotFoundException {
        Trainee trainee = traineeRepository.findByUsername(trainingDto.getTraineeUsername())
                .orElseThrow(() -> new UserNotFoundException("Can't find user with username " + trainingDto.getTraineeUsername()));

        Coach coach = coachRepository.findByUsername(trainingDto.getCoachUsername())
                .orElseThrow(() -> new UserNotFoundException("Can't find user with username " + trainingDto.getCoachUsername()));
        TrainingType trainingType = trainingTypeService.findByName(trainingDto.getTrainingType());

        // todo create validation if training type and coach specialization are different

        Training training = new Training();
        training.setTrainee(trainee);
        training.setCoach(coach);
        training.setTrainingType(trainingType);
        training.setTrainingDate(trainingDto.getDate());
        training.setTrainingName(trainingDto.getName());
        training.setTrainingDuration(trainingDto.getDuration());
        Training save = repository.save(training);
        return true;
    }

    @Override
    public List<Training> getAllByUsername(String username) {
        return null;
    }
}
