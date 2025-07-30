package com.ahmed.controller;

import com.ahmed.dto.user.UpdateProfileRequest;
import com.ahmed.model.User;
import com.ahmed.security.JwtUtil;
import com.ahmed.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    // get user by email
    @GetMapping
    public ResponseEntity<List<User>> getAll() {

        return ResponseEntity.ok(userService.getAllUsers());
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public ResponseEntity<Object> getProfile(@RequestHeader("Authorization") String token) {
        String email = extractEmail(token);
        return ResponseEntity.ok(userService.getProfileByEmail(email));
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/me")
    public ResponseEntity<?> updateProfile(
            @RequestHeader("Authorization") String token,
            @RequestBody UpdateProfileRequest request
    ) {
        System.out.println("token: " + token);
        String email = extractEmail(token);
        System.out.println("email: " + email);
        String role = jwtUtil.extractRole(token);
        System.out.println("role: " + role);


        if (role.equals("ADMIN")) {
            return ResponseEntity.ok(userService.updateAdminProfile(email, request));
        } else if (role.equals("DRIVER")) {
            return ResponseEntity.ok(userService.updateDriverProfile(email, request));
        } else {
            return ResponseEntity.badRequest().body("Unsupported role");
        }
    }


    private String extractEmail(String header) {
        if (header == null || !header.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid token");
        }
        return jwtUtil.extractEmail(header.substring(7));
    }
}
