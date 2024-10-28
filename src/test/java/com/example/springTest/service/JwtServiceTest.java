package com.example.springTest.service;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import com.example.spring.config.Role;
import com.example.spring.service.JWTService;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JWTServiceTests {

    private JWTService jwtService;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtService = new JWTService();
    }

    @Test
    void testGenerateToken() {
        String username = "sudhar";
        Role role = Role.ROLE_STUDENT; // Update to ROLE_STUDENT or your desired role

        String token = jwtService.generateToken(username, role);

        assertNotNull(token);
        assertTrue(token.startsWith("ey")); // JWT tokens start with "ey"
    }

    @Test
    void testExtractUserName() {
        String username = "sudhar";
        Role role = Role.ROLE_STUDENT; // Update to ROLE_STUDENT or your desired role
        String token = jwtService.generateToken(username, role);

        String extractedUsername = jwtService.extractUserName(token);

        assertEquals(username, extractedUsername);
    }

    @Test
    void testValidateToken_ValidToken() {
        String username = "sudhar";
        Role role = Role.ROLE_STUDENT; // Update to ROLE_STUDENT or your desired role
        String token = jwtService.generateToken(username, role);

        when(userDetails.getUsername()).thenReturn(username);

        boolean isValid = jwtService.validateToken(token, userDetails);

        assertTrue(isValid);
    }

    @Test
    void testValidateToken_InvalidToken() {
        String validUsername = "sudhar";
        String invalidUsername = "raj";
        Role role = Role.ROLE_STUDENT; // Update to ROLE_STUDENT or your desired role
        String token = jwtService.generateToken(validUsername, role);

        when(userDetails.getUsername()).thenReturn(invalidUsername);

        boolean isValid = jwtService.validateToken(token, userDetails);

        assertFalse(isValid);
    }

    @Test
    void testIsTokenExpired() {
        String username = "sudhar";
        Role role = Role.ROLE_STUDENT; // Update to ROLE_STUDENT or your desired role
        String token = jwtService.generateToken(username, role);

        // Sleep for 2 seconds to ensure the token is expired for testing
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        boolean isExpired = jwtService.isTokenExpired(token);
        assertTrue(isExpired);
    }

    @Test
    void testIsTokenNotExpired() {
        String username = "sudhar";
        Role role = Role.ROLE_STUDENT; // Update to ROLE_STUDENT or your desired role
        String token = jwtService.generateToken(username, role);

        // Check if the token is not expired immediately after generation
        boolean isExpired = jwtService.isTokenExpired(token);
        assertFalse(isExpired);
    }

    @Test
    void testExtractExpiration() {
        String username = "sudhar";
        Role role = Role.ROLE_STUDENT; // Update to ROLE_STUDENT or your desired role
        String token = jwtService.generateToken(username, role);

        // You can access expiration through the public method
        Date expirationDate = jwtService.extractClaim(token, Claims::getExpiration);

        assertNotNull(expirationDate);
        assertTrue(expirationDate.after(new Date())); // The expiration date should be in the future
    }
}
