package com.example.spring.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.spring.config.Role;
import com.example.spring.model.Student;
import com.example.spring.model.Teacher;
import com.example.spring.model.UserPrincipal;
import com.example.spring.model.Users;
import com.example.spring.repo.TeacherRepo;
import com.example.spring.repo.UserRepo;
import com.example.spring.repo.StudentRepo;
@Service
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
    private final PasswordEncoder passwordEncoder;
    private final UserRepo repo;

    @Autowired
    private JWTService jwtService;
    @Autowired
    private StudentRepo studentRepository;
    @Autowired
    private AuthenticationManager authManager;
    
    @Autowired
    private TeacherRepo teacherRepository;

    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.repo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }
    //Management registration and login methods
    public Users registerManagement(Users userRequest) {
        String normalizedUsername = userRequest.getUsername().toLowerCase();
        LOG.debug("UserService.registerManagement() => Normalized username for registration: {}", normalizedUsername);
    
        if (userExists(normalizedUsername)) {
            LOG.warn("UserService.registerManagement() => Registration failed: User with username {} already exists.", normalizedUsername);
            throw new RuntimeException("User already exists");
        }
    
        try {
            Users user = new Users();
            user.setUsername(normalizedUsername);
            user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
            user.setRole(userRequest.getRole());
            repo.save(user);
            LOG.info("UserService.registerManagement() => User {} registered successfully.", normalizedUsername);
            return user; // Return the registered user
        } catch (Exception e) {
            LOG.error("UserService.registerManagement() => Error while registering user {}: {}", normalizedUsername, e.getMessage());
            throw new RuntimeException("Error registering user");
        }
    }
    
    public String verify(Users user) {
        String normalizedUsername = user.getUsername().toLowerCase();
        LOG.info("UserService.verify() => Authenticating user: {}", normalizedUsername);

        try {
            Users existingUser = repo.findByUsername(normalizedUsername)
                    .orElseThrow(() -> {
                        LOG.warn("UserService.verify() => User not found: {}", normalizedUsername);
                        return new RuntimeException("User not found");
                    });

            if (!passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
                LOG.warn("UserService.verify() => Invalid credentials for user: {}", normalizedUsername);
                throw new RuntimeException("Invalid username or password");
            }

            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(existingUser.getUsername(), user.getPassword()));

            if (authentication.isAuthenticated()) {
                UserPrincipal authenticatedUser = (UserPrincipal) authentication.getPrincipal();
                Role userRole = authenticatedUser.getRole();

                String token = jwtService.generateToken(authenticatedUser.getUsername(), userRole);
                LOG.info("UserService.verify() => Token generated for user: {}", normalizedUsername);
                return token;
            }

        } catch (Exception e) {
            LOG.error("UserService.verify() => Authentication failed for user: {}", normalizedUsername, e);
            throw new RuntimeException("Authentication failed. Please check your credentials.");
        }

        LOG.warn("UserService.verify() => Authentication failed for user: {}", normalizedUsername);
        return "fail";
    }

    // Teacher registration and login methods
    public Teacher registerTeacher(Teacher teacher) {
        LOG.debug("UserService.registerTeacher() => Attempting to register teacher: {}", teacher.getUsername());
        
        if (teacherRepository.existsByUsername(teacher.getUsername())) {
            LOG.error("UserService.registerTeacher() => Teacher with username {} already exists.", teacher.getUsername());
            throw new RuntimeException("Teacher with this username already exists.");
        }
    
        // Encrypt the password before saving
        teacher.setPassword(passwordEncoder.encode(teacher.getPassword()));
        Teacher savedTeacher = teacherRepository.save(teacher); // Save the teacher and return the saved entity
        LOG.info("UserService.registerTeacher() => Teacher {} registered successfully.", savedTeacher.getUsername());
        
        return savedTeacher; // Return the registered teacher
    }
    
    public String verifyTeacherLogin(String username, String password) {
        LOG.info("UserService.verifyTeacherLogin() => Verifying teacher login for username: {}", username);
        Teacher teacher = teacherRepository.findByUsername(username)
                .orElseThrow(() -> {
                    LOG.warn("UserService.verifyTeacherLogin() => Teacher not found: {}", username);
                    return new RuntimeException("Teacher not found.");
                });

        if (!passwordEncoder.matches(password, teacher.getPassword())) {
            LOG.error("UserService.verifyTeacherLogin() => Invalid password for teacher: {}", username);
            throw new RuntimeException("Invalid password.");
        }

        String token = jwtService.generateToken(teacher.getUsername(), Role.TEACHER);
        LOG.info("UserService.verifyTeacherLogin() => Token generated for teacher: {}", username);
        return token;
    }
    // Student registration and login methods
    public Student registerStudent(Student student) {
        if (studentRepository.existsByUsername(student.getUsername())) {
            throw new RuntimeException("Student with this username already exists.");
        }
    
        // Encrypt the password before saving
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        Student savedStudent = studentRepository.save(student); // Save the student and return the saved entity
        LOG.info("Student {} registered successfully.", savedStudent.getUsername());
        
        return savedStudent; // Return the registered student
    }
    

public String verifyStudentLogin(String username, String password) {
    Student student = studentRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Student not found."));

    if (!passwordEncoder.matches(password, student.getPassword())) {
        throw new RuntimeException("Invalid password.");
    }

    return jwtService.generateToken(student.getUsername(), Role.STUDENT);
}

public boolean userExists(String username) {
    boolean exists = repo.findByUsername(username).isPresent();
    LOG.debug("UserService.userExists() => Checking existence of user {}: {}", username, exists);
    return exists;
}

}