package com.example.spring.repo;

import com.example.spring.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepo extends JpaRepository<Teacher, Integer> {
    Optional<Teacher> findByUsername(String username); // Method to find a teacher by username
    boolean existsByUsername(String username); // Method to check if a username already exists
}
