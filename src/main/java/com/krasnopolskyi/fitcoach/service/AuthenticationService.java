package com.krasnopolskyi.fitcoach.service;

import com.krasnopolskyi.fitcoach.dto.request.UserCredentials;
import com.krasnopolskyi.fitcoach.exception.AuthnException;
import com.krasnopolskyi.fitcoach.exception.EntityException;
import com.krasnopolskyi.fitcoach.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final UserServiceImpl userServiceImpl;
    private final JwtService jwtService;


    public String logIn(UserCredentials userCredentials) throws EntityException, AuthnException {
        if (userServiceImpl.checkCredentials(userCredentials)) {
            return "Authentication is successful";
        } else {
            throw new AuthnException("Invalid credentials");
        }
    }

    public boolean isTokenValid(String token) {
        try {
            // Extract the username from the token
            String username = jwtService.extractUserName(token);
            // Validate token by comparing with the actual user credentials
            return jwtService.isTokenValid(token, username);
        } catch (Exception e) {
            log.error("Token validation failed: {}", e.getMessage());
            return false;
        }
    }
}
