package com.krasnopolskyi.fitcoach.service;

import com.krasnopolskyi.fitcoach.dto.request.TrainingDto;
import com.krasnopolskyi.fitcoach.dto.request.TrainingFilterDto;
import com.krasnopolskyi.fitcoach.dto.response.TrainingResponseDto;
import com.krasnopolskyi.fitcoach.entity.*;
import com.krasnopolskyi.fitcoach.exception.EntityException;
import com.krasnopolskyi.fitcoach.exception.ValidateException;
import com.krasnopolskyi.fitcoach.repository.TraineeRepository;
import com.krasnopolskyi.fitcoach.repository.TrainerRepository;
import com.krasnopolskyi.fitcoach.repository.TrainingRepository;
import com.krasnopolskyi.fitcoach.repository.UserRepository;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {
    @Mock
    private TrainingRepository trainingRepository;

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MeterRegistry meterRegistry;

    @InjectMocks
    private TrainingService trainingService;

    private Trainee mockTrainee;
    private User mockUser;

    private Trainer mockTrainer;
    private User mockUserTrainer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        trainingService = new TrainingService(trainingRepository, traineeRepository, trainerRepository, userRepository, meterRegistry);

        mockUser = new User();
        mockUser.setUsername("john.doe");
        mockUser.setFirstName("John");
        mockUser.setLastName("Doe");
        mockUser.setPassword("password123");
        mockUser.setIsActive(true);

        mockTrainee = new Trainee();
        mockTrainee.setUser(mockUser);
        mockTrainee.setAddress("123 Test St");

        mockUserTrainer = new User();
        mockUserTrainer.setUsername("trainer.doe");
        mockUserTrainer.setFirstName("Trainer");
        mockUserTrainer.setLastName("Doe");
        mockUserTrainer.setPassword("password123");
        mockUserTrainer.setIsActive(true);

        mockTrainer = new Trainer();
        mockTrainer.setUser(mockUserTrainer);
        mockTrainer.setSpecialization(new TrainingType(1, "Cardio"));
    }

    @Test
    void save_shouldReturnTrainingResponseDto_whenSuccessful() throws EntityException, ValidateException {
        // Arrange

        TrainingDto trainingDto = new TrainingDto(
                mockTrainee.getUser().getUsername(),
                mockTrainer.getUser().getUsername(),
                "Training1",
                LocalDate.now(),
                60);

        mockTrainee.getTrainers().add(mockTrainer);
        mockTrainer.getTrainees().add(mockTrainee);

        when(traineeRepository.findByUsername(anyString())).thenReturn(Optional.of(mockTrainee));
        when(trainerRepository.findByUsername(anyString())).thenReturn(Optional.of(mockTrainer));

        // Act
        TrainingResponseDto response = trainingService.save(trainingDto);

        // Assert
        assertNotNull(response);
        assertEquals(trainingDto.getTrainingName(), response.trainingName());
        verify(traineeRepository, times(1)).findByUsername(mockTrainee.getUser().getUsername());
        verify(trainerRepository, times(1)).findByUsername(mockTrainer.getUser().getUsername());
        verify(trainingRepository, times(1)).save(any(Training.class));
    }

    @Test
    void save_shouldThrowEntityException_whenTraineeNotFound() {
        // Arrange
        TrainingDto trainingDto = new TrainingDto(
                mockTrainee.getUser().getUsername(),
                mockTrainer.getUser().getUsername(),
                "Training1",
                LocalDate.now(),
                60);
        when(traineeRepository.findByUsername(mockTrainee.getUser().getUsername())).thenReturn(Optional.empty());

        // Act & Assert
        EntityException exception = assertThrows(EntityException.class, () -> {
            trainingService.save(trainingDto);
        });
        assertEquals("Could not find trainee with " + mockTrainee.getUser().getUsername(), exception.getMessage());
        verify(traineeRepository, times(1)).findByUsername(mockTrainee.getUser().getUsername());
        verify(trainingRepository, never()).save(any(Training.class));
    }

    @Test
    void save_shouldThrowEntityException_whenTrainerNotFound() {
        // Arrange
        TrainingDto trainingDto = new TrainingDto(
                mockTrainee.getUser().getUsername(),
                mockTrainer.getUser().getUsername(),
                "Training1",
                LocalDate.now(),
                60);

        Trainee trainee = new Trainee();
        when(traineeRepository.findByUsername(mockTrainee.getUser().getUsername())).thenReturn(Optional.of(trainee));
        when(trainerRepository.findByUsername(mockTrainer.getUser().getUsername())).thenReturn(Optional.empty());

        // Act & Assert
        EntityException exception = assertThrows(EntityException.class, () -> {
            trainingService.save(trainingDto);
        });
        assertEquals("Could not find trainer with id " + mockTrainer.getUser().getUsername(), exception.getMessage());
        verify(trainerRepository, times(1)).findByUsername(mockTrainer.getUser().getUsername());
        verify(trainingRepository, never()).save(any(Training.class));
    }

    @Test
    void getFilteredTrainings_shouldReturnListOfTrainingResponseDto_whenSuccessful() throws EntityException {
        // Arrange
        TrainingDto trainingDto = new TrainingDto(
                mockTrainee.getUser().getUsername(),
                mockTrainer.getUser().getUsername(),
                "Training1",
                LocalDate.now(),
                60);

        when(userRepository.findByUsername(mockTrainee.getUser().getUsername())).thenReturn(Optional.of(mockTrainee.getUser()));


        Training training = new Training();
        training.setTrainee(mockTrainee);
        training.setTrainer(mockTrainer);
        training.setDuration(60);
        training.setTrainingName("Training1");
        training.setDate(LocalDate.now());
        training.setTrainingType(mockTrainer.getSpecialization());

        TrainingFilterDto filter = new TrainingFilterDto();
        filter.setOwner(mockTrainee.getUser().getUsername());

        List<Training> trainings = List.of(training);
        when(trainingRepository.getFilteredTrainings(mockTrainee.getUser().getUsername(), null, null, null, null))
                .thenReturn(trainings);

        // Act
        List<TrainingResponseDto> responseList = trainingService.getFilteredTrainings(filter);

        // Assert
        assertEquals(1, responseList.size());
        assertEquals("Training1", responseList.get(0).trainingName());
        verify(userRepository, times(1)).findByUsername(mockTrainee.getUser().getUsername());
        verify(trainingRepository, times(1)).getFilteredTrainings(mockTrainee.getUser().getUsername(), null, null, null, null);
    }

    @Test
    void getFilteredTrainings_shouldThrowEntityException_whenOwnerNotFound() {
        // Arrange
        String ownerUsername = "owner";
        TrainingFilterDto filter = new TrainingFilterDto(ownerUsername, LocalDate.now().minusDays(10), LocalDate.now(), "Yoga", "partner");

        when(userRepository.findByUsername(ownerUsername)).thenReturn(Optional.empty());

        // Act & Assert
        EntityException exception = assertThrows(EntityException.class, () -> {
            trainingService.getFilteredTrainings(filter);
        });
        assertEquals("Could not found user: " + ownerUsername, exception.getMessage());
        verify(userRepository, times(1)).findByUsername(ownerUsername);
        verify(trainingRepository, never()).getFilteredTrainings(anyString(), anyString(), any(), any(), anyString());
    }
}
