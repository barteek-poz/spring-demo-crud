package com.example.spring_demo_crud.service;

import com.example.spring_demo_crud.dao.RefreshTokenRepository;
import com.example.spring_demo_crud.dao.UserRepository;
import com.example.spring_demo_crud.dto.AuthResponse;
import com.example.spring_demo_crud.dto.RefreshTokenDto;
import com.example.spring_demo_crud.entity.RefreshToken;
import com.example.spring_demo_crud.entity.User;
import com.example.spring_demo_crud.exception.UserAlreadyExistsException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ResponseStatusException;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HexFormat;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public AuthServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository, RefreshTokenRepository refreshTokenRepository, JwtService jwtService, CustomUserDetailsService userDetailsService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
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

    @Override
    public String createRefreshToken(User user) {
        String raw = UUID.randomUUID().toString();
        String hash = hashToken(raw);
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setTokenHash(hash);
        refreshToken.setExpiresAt(Instant.now().plus(30, ChronoUnit.DAYS));
        refreshToken.setRevoked(false);

        refreshTokenRepository.save(refreshToken);

        return raw;
    }

    @Override
    public AuthResponse refresh(String refreshTokenRaw) {
        String hashToken = hashToken(refreshTokenRaw);
        RefreshToken token = refreshTokenRepository.findByTokenHash(hashToken).orElseThrow(()-> new ResponseStatusException(UNAUTHORIZED,"Refresh token expired"));
        if(token.isRevoked() || token.getExpiresAt().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new ResponseStatusException(UNAUTHORIZED, "Refresh token expired");
        }
        token.setRevoked(true);
        refreshTokenRepository.save(token);

        User user = token.getUser();
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());

        String newAccessToken = jwtService.generateToken(userDetails);
        String newRefreshToken = createRefreshToken(user);
        return new AuthResponse(newAccessToken, newRefreshToken);
    }

    private String hashToken(String rawToken) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(rawToken.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }
}


