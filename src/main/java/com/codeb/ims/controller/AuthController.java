package com.codeb.ims.controller;

import com.codeb.ims.entity.User;
import com.codeb.ims.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // Changed to '*' to prevent any CORS issues during testing
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    // 1. REGISTER
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        // Safety Check: Email
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        // --- ROLE LOGIC (The Fix) ---
        // If the request doesn't specify a role, default to "STAFF" (Safe Mode)
        // If the request sends "ADMIN", it will set as ADMIN.
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("STAFF");
        }

        return ResponseEntity.ok(userRepository.save(user));
    }

    // 2. LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        String email = loginData.get("email");
        String password = loginData.get("password");

        Optional<User> user = userRepository.findByEmail(email);

        // Simple Password Check
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            // This returns the FULL User object (including "role": "ADMIN" or "STAFF")
            // The Frontend will read this 'role' to decide which buttons to hide.
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.status(401).body("Invalid Credentials");
        }
    }
}