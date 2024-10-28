package com.example.spring.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.spring.config.Role;

import java.util.Collection;
import java.util.List;

@Entity
public class Users implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private String username;

    private String password;

    @Enumerated(EnumType.STRING) // Store the role as a string in the database
    private Role role; // Change from String to Role enum

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Return the role as a GrantedAuthority, prefixed with "ROLE_"
        return List.of(() ->  role.name());
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() { // Ensure this method exists
        return role; // Update to return Role type
    }

    public void setRole(Role role) { // Ensure this method exists
        this.role = role; // Update to accept Role type
    }

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role + // Update to display Role enum
                '}';
    }

    // Implement remaining UserDetails methods
    @Override
    public boolean isAccountNonExpired() {
        return true; // Customize based on your requirements
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Customize based on your requirements
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Customize based on your requirements
    }

    @Override
    public boolean isEnabled() {
        return true; // Customize based on your requirements
    }
}
