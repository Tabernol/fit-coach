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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private UserServiceImpl userServiceImpl;

    @Mock
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authenticationService = new AuthenticationService(userServiceImpl, jwtService);
    }

    @Test
    void logIn_ValidCredentials_ShouldReturnToken() throws EntityException, AuthnException {
        // Arrange
        UserCredentials credentials = new UserCredentials("testUser", "testPassword");
        String expectedToken = "testToken";

        when(userServiceImpl.checkCredentials(credentials)).thenReturn(true);
        when(jwtService.generateToken(credentials.username())).thenReturn(expectedToken);

        // Act
        String actualToken = authenticationService.logIn(credentials);

        // Assert
        assertEquals(expectedToken, actualToken);
    }

    @Test
    void logIn_InvalidCredentials_ShouldThrowAuthnException() throws EntityException {
        // Arrange
        UserCredentials credentials = new UserCredentials("testUser", "testPassword");

        when(userServiceImpl.checkCredentials(credentials)).thenReturn(false);

        // Act & Assert
        AuthnException exception = assertThrows(AuthnException.class, () -> {
            authenticationService.logIn(credentials);
        });

        assertEquals("Invalid credentials", exception.getMessage());
    }

    @Test
    void isTokenValid_ValidToken_ShouldReturnTrue() {
        // Arrange
        String token = "validToken";
        String username = "testUser";

        when(jwtService.extractUserName(token)).thenReturn(username);
        when(jwtService.isTokenValid(token, username)).thenReturn(true);

        // Act
        boolean isValid = authenticationService.isTokenValid(token);

        // Assert
        assertTrue(isValid);
    }

    @Test
    void isTokenValid_InvalidToken_ShouldReturnFalse() {
        // Arrange
        String token = "invalidToken";

        when(jwtService.extractUserName(token)).thenThrow(new RuntimeException("Invalid token"));

        // Act
        boolean isValid = authenticationService.isTokenValid(token);

        // Assert
        assertFalse(isValid);
    }

}
