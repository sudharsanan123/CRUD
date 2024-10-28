package com.example.spring.model;

import com.example.spring.config.Role;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generate the ID
    private int id;

    // Change name to username
    private String username; // Changed from 'name' to 'username'
    private String password;
    private String subject;
    private Role role; // Add role field

    // No-argument constructor for JPA
    public Teacher() {}

    // Constructor for easy object creation
    public Teacher(int id, String username, String password, String subject, Role role) {
        this.id = id;
        this.username = username; // Updated from 'name' to 'username'
        this.password = password;
        this.subject = subject;
        this.role = role; // Initialize role
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username; // Updated getter
    }

    public void setUsername(String username) {
        this.username = username; // Updated setter
    }

    public String getPassword() {
        return password; // Getter for password
    }

    public void setPassword(String password) {
        this.password = password; // Setter for password
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Role getRole() { // Getter for role
        return role;
    }

    public void setRole(Role role) { // Setter for role
        this.role = role;
    }

    // Override toString for easier logging and debugging
    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + id +
                ", username='" + username + '\'' + // Updated to 'username'
                ", subject='" + subject + '\'' +
                ", role=" + role + // Include role in toString
                '}';
    }
}
