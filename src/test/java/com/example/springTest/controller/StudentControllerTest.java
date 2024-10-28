package com.example.springTest.controller;

import com.example.spring.controller.StudentController;
import com.example.spring.model.Student;
import com.example.spring.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetStudents() throws Exception {
        List<Student> students = new ArrayList<>();
        students.add(new Student(1, "Sudhar", "sudhar@example.com"));
        students.add(new Student(2, "Raj", "raj@example.com"));

        when(studentService.getStudents()).thenReturn(students);

        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Sudhar"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Raj"));
    }

    @Test
    void testAddStudent() throws Exception {
        Student student = new Student(1, "Sudhar", "sudhar@example.com");
        when(studentService.addStudent(any(Student.class))).thenReturn(student);

        mockMvc.perform(post("/students/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Sudhar"));
    }

    @Test
    void testUpdateStudent() throws Exception {
        Student updatedStudent = new Student(1, "Sudhar Updated", "sudhar_updated@example.com");

        when(studentService.updateStudent(anyInt(), any(Student.class))).thenReturn(updatedStudent);

        mockMvc.perform(put("/students/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedStudent)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Sudhar Updated"));
    }

    @Test
    void testDeleteStudent() throws Exception {
        when(studentService.deleteStudent(anyInt())).thenReturn(true);

        mockMvc.perform(delete("/students/{id}", 1))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteStudentNotFound() throws Exception {
        when(studentService.deleteStudent(anyInt())).thenReturn(false);

        mockMvc.perform(delete("/students/{id}", 99))
                .andExpect(status().isNotFound());
    }
}
