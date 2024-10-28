package com.example.spring.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.spring.model.Teacher;
import com.example.spring.service.TeacherService;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/teachers")
public class TeacherController {

    private static final Logger logger = LoggerFactory.getLogger(TeacherController.class);
    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

  @GetMapping("/me")
    public ResponseEntity<Teacher> getLoggedTeacher(Principal principal) {
        String username = principal.getName(); // Getting logged-in username from JWT token
        Teacher teacher = teacherService.findTeacherByUsername(username);
        if (teacher == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(teacher, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    // @PreAuthorize("hasAnyRole('MANAGEMENT', 'TEACHER')")
    public ResponseEntity<Teacher> updateTeacher(@PathVariable int id, @RequestBody Teacher teacher) {
        logger.debug("Received request to update teacher with ID: {}", id);
        Teacher updatedTeacher = teacherService.updateTeacher(id, teacher);
        logger.info("Teacher with ID {} updated successfully", id);
        return ResponseEntity.ok(updatedTeacher);
    }

    @DeleteMapping("/{id}")
    // @PreAuthorize("hasRole('MANAGEMENT')")
    public ResponseEntity<Void> deleteTeacher(@PathVariable int id) {
        logger.debug("Received request to delete teacher with ID: {}", id);
        teacherService.deleteTeacher(id);
        logger.info("Teacher with ID {} deleted successfully", id);
        return ResponseEntity.noContent().build();
    }
}
