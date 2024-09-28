package com.krasnopolskyi.fitcoach.dto.training;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class TrainingDto {
    private String coachUsername;
    private String traineeUsername;
    private String name;
    private String trainingType;
    private LocalDate date;
    private Integer duration; // seconds
}
