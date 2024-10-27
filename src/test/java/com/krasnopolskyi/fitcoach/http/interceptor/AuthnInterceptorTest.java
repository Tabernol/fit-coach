package com.krasnopolskyi.fitcoach.http.interceptor;
import com.krasnopolskyi.fitcoach.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    void preHandle_ShouldReturnFalseAndSetUnauthorizedResponse_WhenTokenIsMissing() throws Exception {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn(null);

        // Act
        boolean result = authnInterceptor.preHandle(request, response, null);

        // Assert
        assertFalse(result);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).setContentType("application/json");
        verify(writer).write("{\"error\": \"Unauthorized: Missing token\"}");
    }

    @Test
    void preHandle_ShouldReturnFalseAndSetUnauthorizedResponse_WhenTokenIsInvalid() throws Exception {
        // Arrange
        String invalidToken = "Bearer invalid_token";
        when(request.getHeader("Authorization")).thenReturn(invalidToken);
        when(authenticationService.isTokenValid("invalid_token")).thenReturn(false);

        // Act
        boolean result = authnInterceptor.preHandle(request, response, null);

        // Assert
        assertFalse(result);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).setContentType("application/json");
        verify(writer).write("{\"error\": \"Unauthorized: Invalid token\"}");
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
