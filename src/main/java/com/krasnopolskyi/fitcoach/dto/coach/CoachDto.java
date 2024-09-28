package com.krasnopolskyi.fitcoach.dto.coach;

import com.krasnopolskyi.fitcoach.dto.trainee.TraineeDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CoachDto {
    private String firstName;
    private String lastName;
    private boolean isActive;
    private String specialization;
    List<TraineeDto> trainees;
}
