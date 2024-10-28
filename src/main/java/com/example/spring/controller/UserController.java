package com.example.spring.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.spring.config.Role;
import com.example.spring.model.Student;
import com.example.spring.model.Teacher;
import com.example.spring.model.Users;
import com.example.spring.service.UserService;



@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Register Management (only Management users can do this)
    @PostMapping("/registerManagement")
    public ResponseEntity<Map<String, String>> registerManagement(@RequestBody Users userRequest) {
    if (userRequest.getRole() != Role.MANAGEMENT) {
        logger.warn("Attempt to register a non-management user.");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
    }
    
    Users registeredUser = userService.registerManagement(userRequest);
    logger.info("Management user registered successfully: {}", registeredUser.getUsername());
    
    Map<String, String> response = new HashMap<>();
    response.put("message", "Management user registered successfully.");
    response.put("username", registeredUser.getUsername());
    response.put("role", registeredUser.getRole().name());
    
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
}


    // Register Teacher (only Management users can do this)
    @PostMapping("/registerTeacher")
    public ResponseEntity<Map<String, String>> registerTeacher(@RequestBody Teacher teacherRequest) {
    try {
        Teacher registeredTeacher = userService.registerTeacher(teacherRequest);
        logger.info("Teacher registered successfully: {}", registeredTeacher.getUsername());
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Teacher registered successfully.");
        response.put("username", registeredTeacher.getUsername());
        response.put("role", registeredTeacher.getRole().name()); // Assuming Teacher class has a role field
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    } catch (RuntimeException e) {
        logger.error("Error registering teacher {}: {}", teacherRequest.getUsername(), e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
}


@PostMapping("/registerStudent")
@PreAuthorize("hasRole('MANAGEMENT') or hasRole('TEACHER')")
public ResponseEntity<Map<String, String>> registerStudent(@RequestBody Student studentRequest) {
    try {
        Student registeredStudent = userService.registerStudent(studentRequest);
        logger.info("Student registered successfully: {}", registeredStudent.getUsername());
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Student registered successfully.");
        response.put("username", registeredStudent.getUsername());
        response.put("role", registeredStudent.getRole()); 

        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    } catch (RuntimeException e) {
        logger.error("Error registering student {}: {}", studentRequest.getUsername(), e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
}

    // User login
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Users user) {
        try {
            String token = userService.verify(user);
            logger.info("User {} logged in successfully.", user.getUsername());
            return ResponseEntity.ok(token);
        } catch (RuntimeException e) {
            logger.error("Login error for user {}: {}", user.getUsername(), e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
    @PostMapping("/login/teacher") // Endpoint for teacher login
    public ResponseEntity<String> teacherLogin(@RequestBody Teacher teacher) {
    try {
        String token = userService.verifyTeacherLogin(teacher.getUsername(), teacher.getPassword());
        return ResponseEntity.ok(token);
    } catch (RuntimeException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }
}

    // Student login
    @PostMapping("/login/student") // Endpoint for student login
    public ResponseEntity<String> studentLogin(@RequestBody Student student) {
        try {
            String token = userService.verifyStudentLogin(student.getUsername(), student.getPassword());
            return ResponseEntity.ok(token);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
