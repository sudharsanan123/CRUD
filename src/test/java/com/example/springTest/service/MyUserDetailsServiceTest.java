package com.example.springTest.service;

import com.example.spring.model.Users;
import com.example.spring.repo.UserRepo;
import com.example.spring.service.MyUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MyUserDetailsServiceTest {

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private MyUserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadUserByUsernameSuccess() {
        Users user = new Users();
        user.setUsername("testUser");

        when(userRepo.findByUsername("testUser")).thenReturn(user);

        assertNotNull(userDetailsService.loadUserByUsername("testUser"));
    }

    @Test
    void testLoadUserByUsernameFailure() {
        when(userRepo.findByUsername("testUser")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("testUser");
        });
    }
}
