package com.example.springTest.service;

import com.example.spring.config.Role; // Import Role
import com.example.spring.model.Users;
import com.example.spring.repo.UserRepo;
import com.example.spring.service.JWTService;
import com.example.spring.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService; // UserService will be instantiated with mocked dependencies

    @Mock
    private UserRepo userRepo; // Mock for UserRepo

    @Mock
    private AuthenticationManager authManager; // Mock for AuthenticationManager

    @Mock
    private JWTService jwtService; // Mock for JWTService

    @Mock
    private Authentication authentication; // Mock for Authentication

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Test
    void testRegisterUser_Success() {
        Users user = new Users();
        user.setUsername("sudhar");
        user.setPassword("password123");

        when(userRepo.findByUsername(user.getUsername())).thenReturn(null); // Mock behavior
        when(userRepo.save(any(Users.class))).thenReturn(user); // Mock save behavior

        Users registeredUser = userService.register(user); // Call the method under test

        assertNotNull(registeredUser); // Validate the result
        assertEquals("sudhar", registeredUser.getUsername());
        assertNotEquals("password123", registeredUser.getPassword()); // Password should be encoded
        assertTrue(new BCryptPasswordEncoder().matches("password123", registeredUser.getPassword())); // Verify password match
    }

    @Test
    void testRegisterUser_UsernameTaken() {
        Users user = new Users();
        user.setUsername("sudhar");
        user.setPassword("password123");

        when(userRepo.findByUsername(user.getUsername())).thenReturn(user); // Mock behavior for existing username

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.register(user)); // Expect exception

        assertEquals("Username is already taken.", exception.getMessage());
    }

    @Test
    void testVerifyUser_Success() {
        Users user = new Users();
        user.setUsername("sudhar");
        user.setPassword("password123");

        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication); // Mock authentication
        when(authentication.isAuthenticated()).thenReturn(true); // Mock successful authentication
        when(jwtService.generateToken(user.getUsername(), Role.ROLE_STUDENT)).thenReturn("mockedToken"); // Use Role enum

        String token = userService.verify(user); // Call the method under test

        assertEquals("mockedToken", token); // Validate the result
    }

    @Test
    void testVerifyUser_Failure() {
        Users user = new Users();
        user.setUsername("sudhar");
        user.setPassword("wrongpassword");

        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new RuntimeException("Authentication failed.")); // Mock failure

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.verify(user)); // Expect exception

        assertEquals("Authentication failed. Please check your credentials.", exception.getMessage());
    }
}
