package com.krasnopolskyi.fitcoach.http.interceptor;
import com.krasnopolskyi.fitcoach.exception.AuthnException;
import com.krasnopolskyi.fitcoach.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthnInterceptorTest {

    private AuthnInterceptor authnInterceptor;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private PrintWriter writer;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        authnInterceptor = new AuthnInterceptor(authenticationService);

        // Mock the response's PrintWriter
        when(response.getWriter()).thenReturn(writer);
    }

    @Test
    void preHandle_ShouldThrowExceptionAndSetUnauthorizedResponse_WhenTokenIsMissing() throws Exception {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn(null);

        // Act
        assertThrows(AuthnException.class,() ->  authnInterceptor.preHandle(request, response, null));

    }

    @Test
    void preHandle_ShouldReturnFalseAndSetUnauthorizedResponse_WhenTokenIsInvalid() throws Exception {
        // Arrange
        String invalidToken = "Bearer invalid_token";
        when(request.getHeader("Authorization")).thenReturn(invalidToken);

        // Act
        assertThrows(AuthnException.class,() ->  authnInterceptor.preHandle(request, response, null));
    }

    @Test
    void preHandle_ShouldReturnTrue_WhenTokenIsValid() throws Exception {
        // Arrange
        String validToken = "Bearer valid_token";
        when(request.getHeader("Authorization")).thenReturn(validToken);
        when(authenticationService.isTokenValid("valid_token")).thenReturn(true);

        // Act
        boolean result = authnInterceptor.preHandle(request, response, null);

        // Assert
        assertTrue(result);
    }
}
