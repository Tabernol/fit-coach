package com.krasnopolskyi.fitcoach.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthnException extends GymException {
    private int code;
    public AuthnException(String message) {
        super(message);
    }
}
