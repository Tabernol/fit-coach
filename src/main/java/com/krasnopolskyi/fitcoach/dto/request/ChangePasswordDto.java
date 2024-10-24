package com.krasnopolskyi.fitcoach.dto.request;

import com.krasnopolskyi.fitcoach.validation.Create;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordDto(
        @NotBlank(groups = Create.class, message = "Username can't be null")
        String username,
        @NotBlank(groups = Create.class, message = "Username can't be null")
        String oldPassword,
        @NotBlank(groups = Create.class, message = "Username can't be null")
        @Size(min = 10)
        String newPassword) {
}
