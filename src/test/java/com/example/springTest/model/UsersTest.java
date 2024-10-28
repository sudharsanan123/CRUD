package com.example.springTest.model;

import org.junit.jupiter.api.Test;

import com.example.spring.model.Users;

import static org.junit.jupiter.api.Assertions.*;

public class UsersTest {

    @Test
    public void testToString() {
        // Create an instance of Users
        Users user = new Users();
        user.setId(1);
        user.setUsername("testUser");
        user.setPassword("testPass");

        // Expected string representation
        String expectedString = "Users{id=1, username='testUser', password='testPass'}";

        // Verify the toString method
        assertEquals(expectedString, user.toString(), "The toString method should return the expected string representation");
    }

    @Test
    public void testUserConstructor() {
        // Create an instance of Users using the constructor
        Users user = new Users();
        user.setId(1);
        user.setUsername("testUser");
        user.setPassword("testPass");

        // Assertions to verify the properties
        assertEquals(1, user.getId(), "The ID should match");
        assertEquals("testUser", user.getUsername(), "The username should match");
        assertEquals("testPass", user.getPassword(), "The password should match");
    }
}
