package com.krasnopolskyi.fitcoach.http.handler;
import com.krasnopolskyi.fitcoach.exception.AuthnException;
import com.krasnopolskyi.fitcoach.exception.EntityException;
import com.krasnopolskyi.fitcoach.exception.ValidateException;
import jakarta.validation.Valid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {
    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private WebRequest webRequest;

    @Mock
    private MethodArgumentNotValidException methodArgumentNotValidException;

    @Mock
    private HttpHeaders headers;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        globalExceptionHandler = new GlobalExceptionHandler();
    }



    @Test
    void handleAllUncaughtException_ShouldReturnInternalServerError() {
        // Arrange
        Exception exception = new Exception("An error occurred");

        // Act
        ResponseEntity<Object> responseEntity = globalExceptionHandler.handleAllUncaughtException(exception, webRequest);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) responseEntity.getBody();
        assertEquals("Sorry, but something went wrong. Try again later", errorResponse.getMessage());
    }

    @Test
    void handleNoSuchElementFoundException_ShouldReturnNotFound() {
        // Arrange
        EntityException exception = new EntityException("Entity not found");

        // Act
        ResponseEntity<Object> responseEntity = globalExceptionHandler.handleNoSuchElementFoundException(exception, webRequest);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) responseEntity.getBody();
        assertEquals("Entity not found", errorResponse.getMessage());
    }

    @Test
    void handleAuthnException_ShouldReturnForbidden() {
        // Arrange
        AuthnException exception = new AuthnException("Authentication failed");

        // Act
        ResponseEntity<Object> responseEntity = globalExceptionHandler.handleAuthnException(exception, webRequest);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) responseEntity.getBody();
        assertEquals("Authentication failed", errorResponse.getMessage());
    }

    @Test
    void handleValidateException() {
        // Arrange
        ValidateException exception = new ValidateException("Validation failed");

        // Act
        ResponseEntity<Object> responseEntity = globalExceptionHandler.handleCustomValidateException(exception, webRequest);

        // Assert
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) responseEntity.getBody();
        assertEquals("Validation failed", errorResponse.getMessage());
    }
}
