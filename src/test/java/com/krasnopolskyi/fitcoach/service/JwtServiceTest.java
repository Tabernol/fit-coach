package com.krasnopolskyi.fitcoach.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    // Sample key for testing (ensure it's valid Base64)
    private final String testJwtSigningKey = "SW1hZ2luYXRpb24gaXMgbW9yZSBpbXBvcnRhbnQgdGhhbiBrbm93bGVkZ2Uu";

    private String testUsername = "testUser";
    private String generatedToken;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Inject the signing key via reflection
        ReflectionTestUtils.setField(jwtService, "jwtSigningKey", testJwtSigningKey);

        // Generate a test token for the test cases
        generatedToken = jwtService.generateToken(testUsername);
    }

    @Test
    void testGenerateToken() {
        assertNotNull(generatedToken, "Token should be generated");
        assertTrue(generatedToken.startsWith("eyJ"), "Token should start with 'eyJ'");
    }

    @Test
    void testExtractUserName() {
        String extractedUsername = jwtService.extractUserName(generatedToken);
        assertEquals(testUsername, extractedUsername, "Extracted username should match");
    }

    @Test
    void testIsTokenValid() {
        boolean isValid = jwtService.isTokenValid(generatedToken, testUsername);
        assertTrue(isValid, "Token should be valid for the correct username");
    }

    @Test
    void testIsTokenInvalid() {
        String anotherUsername = "anotherUser";
        boolean isValid = jwtService.isTokenValid(generatedToken, anotherUsername);
        assertFalse(isValid, "Token should be invalid for a different username");
    }
}
