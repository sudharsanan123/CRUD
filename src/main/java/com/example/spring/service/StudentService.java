package com.example.spring.service;
import com.example.spring.model.Student;
import com.example.spring.repo.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepo studentRepo;

    // Method for Management to add a student (STUDENT role cannot access)
    public Student addStudent(Student student) {
        if (studentRepo.existsByUsername(student.getUsername())) {
            throw new IllegalArgumentException("Duplicate student name: " + student.getUsername());
        }
        return studentRepo.save(student);
    }

    public Student findStudentByUsername(String username) {
        Optional<Student> studentOptional = studentRepo.findByUsername(username);
        return studentOptional.orElse(null);  // Return null if student not found
    }
    

    // Method to get all students - accessible to MANAGEMENT, TEACHER
    public List<Student> getStudents() {
        return studentRepo.findAll();
    }

    // Method to update a student - accessible to MANAGEMENT and TEACHER only
    public Student updateStudent(int id, Student updatedStudent) {
        if (!studentRepo.existsById(id)) {
            throw new IllegalArgumentException("Student with id " + id + " not found.");
        }

        if (studentRepo.existsByUsername(updatedStudent.getUsername()) && studentRepo.findByUsername(updatedStudent.getUsername()).get().getId() != id) {
            throw new IllegalArgumentException("Duplicate student name: " + updatedStudent.getUsername());
        }

        updatedStudent.setId(id);
        return studentRepo.save(updatedStudent);
    }

    // Method to delete a student - accessible to MANAGEMENT only
    public boolean deleteStudent(int id) {
        if (!studentRepo.existsById(id)) {
            throw new IllegalArgumentException("Student with id " + id + " not found.");
        }
        studentRepo.deleteById(id);
        return true;
    }

    // Method to get a student by ID - accessible to MANAGEMENT, TEACHER, and the student themselves
    public Optional<Student> getStudentById(int id) {
        return studentRepo.findById(id);
    }
}
