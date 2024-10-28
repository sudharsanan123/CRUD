package com.example.spring.service;

import com.example.spring.model.Student;
import com.example.spring.model.Teacher;
import com.example.spring.repo.StudentRepo;
import com.example.spring.repo.TeacherRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManagementService {
    private final TeacherRepo teacherRepo;
    private final StudentRepo studentRepo;

    public ManagementService(TeacherRepo teacherRepo, StudentRepo studentRepo) {
        this.teacherRepo = teacherRepo;
        this.studentRepo = studentRepo;
    }

    // Teacher operations
    public List<Teacher> getAllTeachers() {
        return teacherRepo.findAll();
    }

    public Teacher addTeacher(Teacher teacher) {
        return teacherRepo.save(teacher);
    }

    public Teacher updateTeacher(int id, Teacher teacherDetails) {
        Teacher teacher = teacherRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Teacher not found"));
        teacher.setUsername(teacherDetails.getUsername()); // Changed from setName to setUsername
        teacher.setSubject(teacherDetails.getSubject());
        return teacherRepo.save(teacher);
    }

    public void deleteTeacher(int id) {
        teacherRepo.deleteById(id);
    }

    // Student operations
    public List<Student> getAllStudents() {
        return studentRepo.findAll();
    }

    public Student addStudent(Student student) {
        return studentRepo.save(student);
    }

    public Student updateStudent(int id, Student studentDetails) {
        Student student = studentRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Student not found"));
        student.setUsername(studentDetails.getUsername());
        student.setAge(studentDetails.getAge());
        return studentRepo.save(student);
    }

    public void deleteStudent(int id) {
        studentRepo.deleteById(id);
    }
}
