package com.example.spring_demo_crud.service;

import com.example.spring_demo_crud.dao.UserRepository;
import com.example.spring_demo_crud.entity.User;
import com.example.spring_demo_crud.exception.UserAlreadyExistsException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public AuthServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public ResponseEntity<String> createUser(User user){
        try {
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("User was created");
        } catch(DataIntegrityViolationException ex){
            throw new UserAlreadyExistsException("User with email " + user.getEmail() + " already exists");
        }
    }

    @Override
    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}


