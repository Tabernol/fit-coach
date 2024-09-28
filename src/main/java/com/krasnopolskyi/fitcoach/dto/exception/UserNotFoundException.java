package com.krasnopolskyi.fitcoach.dto.exception;

public class UserNotFoundException extends FitCoachException{
    public UserNotFoundException(String message) {
        super(message);
    }
}
