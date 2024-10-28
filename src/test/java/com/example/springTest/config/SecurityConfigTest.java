package com.example.springTest.config;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.spring.config.JwtFilter;
import com.example.spring.config.SecurityConfig;

@EnableWebSecurity
class SecurityConfigTest {

    @InjectMocks
    private SecurityConfig securityConfig;

    @Mock
    private JwtFilter jwtFilter;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private AuthenticationConfiguration authenticationConfiguration;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSecurityFilterChain() throws Exception {
        // Given
        HttpSecurity http = mock(HttpSecurity.class);
        when(http.csrf(any())).thenReturn(http);
        when(http.authorizeHttpRequests(any())).thenReturn(http);
        when(http.httpBasic(any())).thenReturn(http);
        when(http.sessionManagement(any())).thenReturn(http);
        when(http.addFilterBefore(any(), any())).thenReturn(http);
        when(http.build()).thenReturn(mock(SecurityFilterChain.class));

        // When
        securityConfig.securityFilterChain(http);

        // Then
        verify(http).csrf(any());
        verify(http).authorizeHttpRequests(any());
        verify(http).httpBasic(any());
        verify(http).sessionManagement(any());
        verify(http).addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        verify(http).build();
    }

    @Test
    void testAuthenticationProvider() {
        AuthenticationProvider provider = securityConfig.authenticationProvider();

        // Verify the provider's properties
        assertNotNull(provider);
        assertEquals(passwordEncoder, provider.getPasswordEncoder());
        assertEquals(userDetailsService, provider.getUserDetailsService());
    }

    @Test
    void testAuthenticationManager() throws Exception {
        when(authenticationConfiguration.getAuthenticationManager()).thenReturn(authenticationManager);
        
        AuthenticationManager manager = securityConfig.authenticationManager(authenticationConfiguration);

        assertNotNull(manager);
        verify(authenticationConfiguration).getAuthenticationManager();
    }
}
