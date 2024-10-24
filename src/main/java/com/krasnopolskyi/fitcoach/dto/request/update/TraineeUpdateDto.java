package com.krasnopolskyi.fitcoach.dto.request.update;


import java.time.LocalDate;

public class TraineeUpdateDto {
    private String username;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String address;
    private boolean isActive;
}
