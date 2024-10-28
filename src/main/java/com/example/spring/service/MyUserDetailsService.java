package com.example.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.spring.model.Teacher;
import com.example.spring.model.Student; // Import the Student model
import com.example.spring.model.UserPrincipal;
import com.example.spring.model.Users;
import com.example.spring.repo.UserRepo;
import com.example.spring.repo.TeacherRepo;
import com.example.spring.repo.StudentRepo; // Import your StudentRepo

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private TeacherRepo teacherRepo;

    @Autowired
    private StudentRepo studentRepo; // Inject StudentRepo

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // First check in Users repository
        Optional<Users> userOptional = userRepo.findByUsername(username);
        if (userOptional.isPresent()) {
            return new UserPrincipal(userOptional.get());
        }

        // Then check in Teacher repository
        Optional<Teacher> teacherOptional = teacherRepo.findByUsername(username);
        if (teacherOptional.isPresent()) {
            return new UserPrincipal(teacherOptional.get());
        }

        // Finally check in Student repository (assuming you have a similar method)
        Optional<Student> studentOptional = studentRepo.findByUsername(username); 
        if (studentOptional.isPresent()) {
            return new UserPrincipal(studentOptional.get()); 
        }

        // If none found, throw exception
        throw new UsernameNotFoundException("User not found: " + username);
    }
}
