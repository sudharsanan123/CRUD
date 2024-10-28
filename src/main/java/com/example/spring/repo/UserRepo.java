package com.example.spring.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.spring.model.Users;

@Repository
public interface UserRepo extends JpaRepository<Users, Integer> {
    Optional<Users> findByUsername(String username); 
}
