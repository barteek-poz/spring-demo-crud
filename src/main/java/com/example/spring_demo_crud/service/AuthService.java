package com.example.spring_demo_crud.service;

import com.example.spring_demo_crud.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

public interface AuthService {
    boolean existsByEmail(String email);
    ResponseEntity<String> createUser(User user);
    String encodePassword(String password);
}
