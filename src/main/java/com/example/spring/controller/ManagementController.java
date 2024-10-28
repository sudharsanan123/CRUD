package com.example.spring.controller;

import com.example.spring.model.Student;
import com.example.spring.model.Teacher;
import com.example.spring.service.ManagementService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/management")
public class ManagementController {

    private final ManagementService managementService;

    public ManagementController(ManagementService managementService) {
        this.managementService = managementService;

    }

    // Teacher Endpoints
    @GetMapping("/teachers/all")
    public ResponseEntity<List<Teacher>> getAllTeachers() {
        List<Teacher> teachers = managementService.getAllTeachers();
        return ResponseEntity.ok(teachers);
    }

    @PostMapping("/teachers")
    public ResponseEntity<Teacher> addTeacher(@RequestBody Teacher teacher) {
        Teacher createdTeacher = managementService.addTeacher(teacher);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTeacher);
    }

    @PutMapping("/teachers/{id}")
    public ResponseEntity<Teacher> updateTeacher(@PathVariable int id, @RequestBody Teacher teacherDetails) {
        Teacher updatedTeacher = managementService.updateTeacher(id, teacherDetails);
        return ResponseEntity.ok(updatedTeacher);
    }

    @DeleteMapping("/teachers/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable int id) {
        managementService.deleteTeacher(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/registerTeacher")
    public ResponseEntity<String> registerTeacher(@RequestBody Teacher teacher) {
        try {
            managementService.addTeacher(teacher);
            return ResponseEntity.status(HttpStatus.CREATED).body("Teacher registered successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Student Endpoints
    @GetMapping("/students")
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = managementService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    @PostMapping("/students")
    public ResponseEntity<Student> addStudent(@RequestBody Student student) {
        Student createdStudent = managementService.addStudent(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStudent);
    }

    @PutMapping("/students/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable int id, @RequestBody Student studentDetails) {
        Student updatedStudent = managementService.updateStudent(id, studentDetails);
        return ResponseEntity.ok(updatedStudent);
    }

    @DeleteMapping("/students/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable int id) {
        managementService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}
