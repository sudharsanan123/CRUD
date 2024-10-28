package com.example.spring.service;

import com.example.spring.model.Teacher;
import com.example.spring.repo.TeacherRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherService {

    private static final Logger LOG = LoggerFactory.getLogger(TeacherService.class);
    private final TeacherRepo teacherRepo;
    private final PasswordEncoder passwordEncoder;

    public TeacherService(TeacherRepo teacherRepo, PasswordEncoder passwordEncoder) {
        this.teacherRepo = teacherRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public Teacher register(Teacher teacher) {
        LOG.debug("TeacherService.register() => Attempting to register teacher: {}", teacher.getUsername());
        if (teacherRepo.existsByUsername(teacher.getUsername())) {
            LOG.error("TeacherService.register() => Duplicate teacher username: {}", teacher.getUsername());
            throw new IllegalArgumentException("Duplicate teacher username: " + teacher.getUsername());
        }

        teacher.setPassword(passwordEncoder.encode(teacher.getPassword()));
        LOG.info("TeacherService.register() => Teacher registered successfully: {}", teacher.getUsername());
        return teacherRepo.save(teacher);
    }

    public Optional<Teacher> getTeacherById(int id) {
        LOG.debug("TeacherService.getTeacherById() => Fetching teacher by ID: {}", id);
        Optional<Teacher> teacher = teacherRepo.findById(id);
        if (teacher.isPresent()) {
            LOG.info("TeacherService.getTeacherById() => Teacher found: {}", teacher.get().getUsername());
        } else {
            LOG.warn("TeacherService.getTeacherById() => Teacher not found with ID: {}", id);
        }
        return teacher;
    }

    public List<Teacher> getAllTeachers() {
        LOG.debug("TeacherService.getAllTeachers() => Fetching all teachers");
        return teacherRepo.findAll();
    }

    public Teacher findTeacherByUsername(String username) {
        Optional<Teacher> teacherOptional = teacherRepo.findByUsername(username);
        return teacherOptional.orElse(null);  // Return null if teacher not found
    }

    public Teacher updateTeacher(int id, Teacher teacher) {
        LOG.debug("TeacherService.updateTeacher() => Attempting to update teacher with ID: {}", id);
        Optional<Teacher> existingTeacher = teacherRepo.findById(id);
        if (!existingTeacher.isPresent()) {
            LOG.error("TeacherService.updateTeacher() => Teacher not found for update with ID: {}", id);
            throw new IllegalArgumentException("Teacher not found.");
        }
        if (teacherRepo.existsByUsername(teacher.getUsername()) && 
            !existingTeacher.get().getUsername().equals(teacher.getUsername())) {
            LOG.error("TeacherService.updateTeacher() => Duplicate teacher username during update: {}", teacher.getUsername());
            throw new IllegalArgumentException("Duplicate teacher username.");
        }

        if (teacher.getPassword() != null) {
            LOG.debug("TeacherService.updateTeacher() => Encoding password for teacher: {}", teacher.getUsername());
            teacher.setPassword(passwordEncoder.encode(teacher.getPassword()));
        }

        teacher.setId(id);
        LOG.info("TeacherService.updateTeacher() => Teacher updated successfully: {}", teacher.getUsername());
        return teacherRepo.save(teacher);
    }

    public void deleteTeacher(int id) {
        LOG.debug("TeacherService.deleteTeacher() => Attempting to delete teacher with ID: {}", id);
        if (!teacherRepo.existsById(id)) {
            LOG.error("TeacherService.deleteTeacher() => Teacher not found for deletion with ID: {}", id);
            throw new IllegalArgumentException("Teacher not found.");
        }
        teacherRepo.deleteById(id);
        LOG.info("TeacherService.deleteTeacher() => Teacher with ID {} deleted successfully.", id);
    }
}
