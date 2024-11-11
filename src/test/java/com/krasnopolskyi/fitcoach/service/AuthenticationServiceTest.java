package com.krasnopolskyi.fitcoach.service;

import com.krasnopolskyi.fitcoach.dto.request.UserCredentials;
import com.krasnopolskyi.fitcoach.exception.AuthnException;
import com.krasnopolskyi.fitcoach.exception.EntityException;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authenticationService = new AuthenticationService(jwtService, authenticationManager);
    }

//    @Test
//    void logIn_ValidCredentials_ShouldReturnToken() throws EntityException, AuthnException {
//        // Arrange
//        UserCredentials credentials = new UserCredentials("testUser", "testPassword");
//        String expectedToken = "testToken";
//
//        when(userServiceImpl.checkCredentials(credentials)).thenReturn(true);
//        when(jwtService.generateToken(credentials.username())).thenReturn(expectedToken);
//
//        // Act
//        String actualToken = authenticationService.logIn(credentials);
//
//        // Assert
//        assertEquals(expectedToken, actualToken);
//    }

//    @Test
//    void logIn_InvalidCredentials_ShouldThrowAuthnException() throws EntityException {
//        // Arrange
//        UserCredentials credentials = new UserCredentials("testUser", "testPassword");
//
//        when(userServiceImpl.checkCredentials(credentials)).thenReturn(false);
//
//        // Act & Assert
//        AuthnException exception = assertThrows(AuthnException.class, () -> {
//            authenticationService.logIn(credentials);
//        });
//
//        assertEquals("Invalid credentials", exception.getMessage());
//    }


}
