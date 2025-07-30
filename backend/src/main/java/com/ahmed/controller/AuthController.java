package com.ahmed.controller;

import com.ahmed.Exception.AuthException;
import com.ahmed.dto.auth.LoginRequest;
import com.ahmed.dto.auth.RegisterRequest;
import com.ahmed.model.User;
import com.ahmed.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;


    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Register endpoint
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterRequest request) {
        User newUser = authService.register(
                request.name,
                request.email,
                request.password,
                request.role
        );
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(newUser);
    }

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        String token = authService.login(
                request.email,
                request.password
        );
        return ResponseEntity.ok(token);
    }
    @ExceptionHandler(AuthException.class)
    public ResponseEntity<String> handleAuthException(AuthException ex) {
        return ResponseEntity.status(401).body(ex.getMessage());
    }
}
