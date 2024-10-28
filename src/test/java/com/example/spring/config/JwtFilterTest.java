package com.example.spring.config;

import com.example.spring.service.JWTService;
import com.example.spring.service.MyUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class JwtFilterTest {

    @InjectMocks
    private JwtFilter jwtFilter;

    @Mock
    private JWTService jwtService;

    @Mock
    private MyUserDetailsService userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private UserDetails userDetails;

    private String token = "mockedJwtToken";
    private String username = "testUser";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        // Set up the mock behavior
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.extractUserName(token)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.validateToken(token, userDetails)).thenReturn(true);
    }

    @Test
    public void testDoFilterInternal_UserAuthenticated_Success() throws IOException, ServletException {
        // Act
        jwtFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response); // Ensure the filter chain continues
        verify(jwtService).extractUserName(token); // Ensure username extraction occurred
        verify(userDetailsService).loadUserByUsername(username); // Ensure user details loading
        verify(jwtService).validateToken(token, userDetails); // Ensure token validation
    }

    @Test
    public void testDoFilterInternal_ValidToken_Authenticated() throws ServletException, IOException {
        // Arrange
        String token = "validToken"; // Provide a valid token that your service recognizes
        String username = "testUser"; // The user you are testing

        // Mock the behavior of the UserDetailsService
        when(userDetailsService.loadUserByUsername(username))
                .thenReturn(new User(username, "password", new ArrayList<>())); // Correct User type

        // Mock the JWT validation to return the username
        when(jwtService.extractUserName(token)).thenReturn(username);

        // Act
        jwtFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(userDetailsService).loadUserByUsername(username); // Ensure it was called
    }

    @Test
    public void testDoFilterInternal_NoToken_NoAuthentication() throws IOException, ServletException {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn(null); // Simulate no token

        // Act
        jwtFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response); // Ensure the filter chain continues
        verify(jwtService, never()).extractUserName(any()); // Ensure username extraction did not occur
        verify(userDetailsService, never()).loadUserByUsername(any()); // Ensure user details loading did not occur
        verify(jwtService, never()).validateToken(any(), any()); // Ensure token validation did not occur
    }
}
