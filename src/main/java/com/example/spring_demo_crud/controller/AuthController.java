package com.example.spring_demo_crud.controller;

import com.example.spring_demo_crud.dao.UserRepository;
import com.example.spring_demo_crud.dto.AuthRequest;
import com.example.spring_demo_crud.dto.AuthResponse;
import com.example.spring_demo_crud.entity.User;
import com.example.spring_demo_crud.service.AuthService;
import com.example.spring_demo_crud.service.JwtService;
import com.example.spring_demo_crud.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder; // dlaczego password jest dekodowany w kontrolerze?

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService, AuthService authService, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public void register(@RequestBody AuthRequest request){
        if(authService.existsByEmail(request.email())){
            throw new RuntimeException("User with this email already exists");
        }
        User user = new User();
        user.setEmail(request.email());
        user.setPassword(authService.encodePassword(request.password()));
        user.setRole("USER");

        authService.createUser(user);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String token = jwtService.generateToken(userDetails);

        return new AuthResponse(token);
    }
}

