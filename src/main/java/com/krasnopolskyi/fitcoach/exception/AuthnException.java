package com.krasnopolskyi.fitcoach.exception;

import org.springframework.security.authentication.BadCredentialsException;

public class AuthnException extends GymException {
    public AuthnException(String message) {
        super(message);
    }

    public AuthnException(String message, Throwable cause) {
        super(message, cause);
    }
}
