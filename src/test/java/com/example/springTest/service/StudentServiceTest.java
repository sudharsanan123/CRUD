package com.example.springTest.service;

import com.example.spring.model.Student;
import com.example.spring.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentServiceTest {

    private StudentService studentService;

    @BeforeEach
    void setUp() {
        studentService = new StudentService();
    }

    @Test
    void testUpdateStudent() {
        // Arrange
        Student student1 = new Student(1, "Sudhar", "sudhar@gmail.com", 21);
        studentService.addStudent(student1); // Add initial student

        Student updatedStudent = new Student(1, "Raj", "raj@gmail.com", 21);

        // Act
        Student result = studentService.updateStudent(1, updatedStudent);

        // Assert
        assertNotNull(result);
        assertEquals("Raj", result.getName());
        assertEquals("raj@gmail.com", result.getEmail());
        assertEquals(21, result.getAge());
    }

    @Test
    void testUpdateStudentNotFound() {
        // Arrange
        Student updatedStudent = new Student(999, "Raj", "raj@gmail.com", 21); // Non-existent ID

        // Act
        Student result = studentService.updateStudent(999, updatedStudent);

        // Assert
        assertNull(result);
    }

    @Test
    void testDeleteStudent() {
        // Arrange
        Student student1 = new Student(1, "Sudhar", "sudhar@gmail.com", 21);
        studentService.addStudent(student1); // Add initial student

        // Act
        boolean isDeleted = studentService.deleteStudent(1);

        // Assert
        assertTrue(isDeleted);
        assertTrue(studentService.getStudents().isEmpty()); // Ensure no students are left
    }

    @Test
    void testDeleteStudentNotFound() {
        // Act
        boolean isDeleted = studentService.deleteStudent(999); // Non-existent ID

        // Assert
        assertFalse(isDeleted); // Deletion should return false
    }
    
    @Test
    void testGetStudents() {
        // Arrange
        studentService.addStudent(new Student(1, "Sudhar", "sudhar@gmail.com", 21));
        studentService.addStudent(new Student(2, "Raj", "raj@gmail.com", 21));

        // Act
        List<Student> students = studentService.getStudents();

        // Assert
        assertEquals(2, students.size());
    }
}
