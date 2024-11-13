package com.krasnopolskyi.fitcoach.service;

import com.krasnopolskyi.fitcoach.exception.AuthnException;
import com.krasnopolskyi.fitcoach.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private LoginBruteForceProtectorService loginProtectorService;

    @Mock
    private UserServiceImpl userDetails;

    private final String USERNAME = "testUser";
    private final String PASSWORD = "testPassword";
    private final String TOKEN = "jwtToken";
    private final String AUTH_HEADER = "Bearer jwtToken";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testLogOut_NoTokenInHeader() {
        // Call the logout method with an invalid authorization header
        AuthnException exception = assertThrows(AuthnException.class, () -> {
            authenticationService.logout("InvalidHeader");
        });

        // Verify behavior
        assertEquals("Token not found in request", exception.getMessage());
    }

    @Test
    void testLogOut_EmptyToken() {
        // Call the logout method with an empty authorization header
        AuthnException exception = assertThrows(AuthnException.class, () -> {
            authenticationService.logout("Bearer ");
        });

        // Verify behavior
        assertEquals("Token not found in request", exception.getMessage());
    }

}
