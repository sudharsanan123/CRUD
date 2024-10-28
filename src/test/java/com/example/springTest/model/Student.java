package com.example.springTest.model;

import org.junit.jupiter.api.Test;
import com.example.spring.model.Student;
import static org.junit.jupiter.api.Assertions.*;

class StudentTest {

    @Test
    void testStudentConstructorAndGetters() {
        // Arrange
        int expectedId = 1; // Changed from Long to int
        String expectedName = "sudhar";
        String expectedEmail = "sudhar@gmail.com";

        // Act
        Student student = new Student(expectedId, expectedName, expectedEmail);

        // Assert
        assertEquals(expectedId, student.getId());
        assertEquals(expectedName, student.getName());
        assertEquals(expectedEmail, student.getEmail());
    }

    @Test
    void testSetters() {
        // Arrange
        Student student = new Student(1, "sudhar", "sudhar@gmail.com");

        // Act
        student.setId(2);
        student.setName("sanan");
        student.setEmail("sanan@gmail.com");

        // Assert
        assertEquals(2, student.getId());
        assertEquals("sanan", student.getName());
        assertEquals("sanan@gmail.com", student.getEmail());
    }
    @Test
    void testEqualsAndHashCode() {
        Student student1 = new Student(1, "sudhar", "sudhar@gmail.com", 20);
        Student student2 = new Student(1, "sudhar", "sudhar@gmail.com", 20);
        Student student3 = new Student(2, "sanan", "sanan@gmail.com", 22);

        // Check equality
        assertEquals(student1, student2, "Students with the same attributes should be equal");
        assertNotEquals(student1, student3, "Students with different attributes should not be equal");

        // Check hashCode
        assertEquals(student1.hashCode(), student2.hashCode(), "Hash codes of equal students should match");
        assertNotEquals(student1.hashCode(), student3.hashCode(), "Hash codes of unequal students should not match");
    }

    @Test
    void testToString() {
        Student student = new Student(1, "sudhar", "sudhar@gmail.com", 20);
        String expectedString = "Student{id=1, name='sudhar', email='sudhar@gmail.com', age=20}";
        
        // Check toString method
        assertEquals(expectedString, student.toString(), "toString should return the correct string representation");
    }

    @Test
    void testSetAge() {
        Student student = new Student(1, "sudhar", "sudhar@gmail.com", 20);
        
        // Change age
        student.setAge(25);
        
        // Check if age is updated
        assertEquals(25, student.getAge(), "Age should be updated to 25");
    }
}
