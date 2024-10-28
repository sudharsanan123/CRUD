package com.example.springTest.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.GrantedAuthority;

import com.example.spring.model.UserPrincipal;
import com.example.spring.model.Users;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserPrincipalTest {

    private Users mockUser;
    private UserPrincipal userPrincipal;

    @BeforeEach
    void setUp() {
        mockUser = Mockito.mock(Users.class);  // Mock the Users class
        userPrincipal = new UserPrincipal(mockUser);  // Create a UserPrincipal instance
    }

    @Test
    void testGetAuthorities() {
        Collection<? extends GrantedAuthority> authorities = userPrincipal.getAuthorities();
        
        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertEquals("USER", authorities.iterator().next().getAuthority());
    }

    @Test
    void testGetPassword() {
        String mockPassword = "password123";
        when(mockUser.getPassword()).thenReturn(mockPassword);

        assertEquals(mockPassword, userPrincipal.getPassword());
    }

    @Test
    void testGetUsername() {
        String mockUsername = "testUser";
        when(mockUser.getUsername()).thenReturn(mockUsername);

        assertEquals(mockUsername, userPrincipal.getUsername());
    }

    @Test
    void testIsAccountNonExpired() {
        assertTrue(userPrincipal.isAccountNonExpired());  // Always returns true
    }

    @Test
    void testIsAccountNonLocked() {
        assertTrue(userPrincipal.isAccountNonLocked());  // Always returns true
    }

    @Test
    void testIsCredentialsNonExpired() {
        assertTrue(userPrincipal.isCredentialsNonExpired());  // Always returns true
    }

    @Test
    void testIsEnabled() {
        assertTrue(userPrincipal.isEnabled());  // Always returns true
    }
}
