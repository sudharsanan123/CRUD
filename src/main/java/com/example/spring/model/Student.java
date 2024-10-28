package com.example.spring.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Objects;

@Entity // Mark this class as an entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment ID starting from 1
    private int id;
    private String username; // Changed from name to username
    private String email;
    private String password; // New field for password
    private int age;
    private String role; // New field for role

    // No-argument constructor
    public Student() {
    }

    // Constructor with id, username, email, and role
    public Student(int id, String username, String email, String password, String role) {
        this(id, username, email, password, 0, role); // Calls the full constructor with age set to 0
    }

    // Constructor with all parameters
    public Student(int id, String username, String email, String password, int age, String role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.age = age;
        this.role = role; // Initialize the role
    }

    // Constructor with username, email, password, age, and role (id defaults to 0)
    public Student(String username, String email, String password, int age, String role) {
        this(1, username, email, password, age, role); // Calls the full constructor with id set to 1
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getRole() {
        return role; // Getter for role
    }

    public void setRole(String role) {
        this.role = role; // Setter for role
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", role='" + role + '\'' + // Include role in the string representation
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Student student = (Student) obj;
        return age == student.age &&
                id == student.id &&
                Objects.equals(username, student.username) && // Updated to compare username
                Objects.equals(email, student.email) &&
                Objects.equals(password, student.password) && // Compare password for equality
                Objects.equals(role, student.role); // Compare role for equality
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email, password, age, role); // Updated to include role
    }
}
