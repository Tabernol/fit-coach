package com.krasnopolskyi.fitcoach.dto.trainee;

import com.krasnopolskyi.fitcoach.dto.coach.CoachInfo;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class TraineeDto {
    private String firstName;
    private String lastName;
    private String username;
    private LocalDate dateOfBirth;
    private String address;
    private boolean isActive;
    private List<CoachInfo> coaches;
}
