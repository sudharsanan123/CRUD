package com.example.spring.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.spring.config.Role;

import java.util.Collection;

public class UserPrincipal implements UserDetails {
    private final int id; 
    private final String username;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;
    private final Role role; // Add role field

    // Constructor for Users
    public UserPrincipal(Users user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.authorities = AuthorityUtils.createAuthorityList(user.getRole().name());
        this.role = user.getRole(); // Set role
    }

    // New Constructor for Teacher
    public UserPrincipal(Teacher teacher) {
        this.id = teacher.getId();
        this.username = teacher.getUsername();
        this.password = teacher.getPassword();
        this.authorities = AuthorityUtils.createAuthorityList("TEACHER"); // Set role authority
        this.role = Role.TEACHER; // Set role for teacher
    }

    // New Constructor for Student
    public UserPrincipal(Student student) {
        this.id = student.getId();
        this.username = student.getUsername();
        this.password = student.getPassword();
        this.authorities = AuthorityUtils.createAuthorityList("STUDENT"); // Ensure this is "STUDENT"
        this.role = Role.STUDENT; // Ensure the role is STUDENT
    }
    

    // Implement other UserDetails methods
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // New method to get role
    public Role getRole() {
        return role;
    }

    // Add any other methods you need, like getId() if necessary
}
