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
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    // 1. REGISTER (Updated with Error Handling)
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            // Safety Check: Email
            if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                return ResponseEntity.badRequest().body("Error: Email already exists");
            }

            // --- ROLE LOGIC ---
            // Default to "STAFF" if not specified
            if (user.getRole() == null || user.getRole().isEmpty()) {
                user.setRole("STAFF");
            }

            User savedUser = userRepository.save(user);
            return ResponseEntity.ok(savedUser);

        } catch (Exception e) {
            // ✅ CRITICAL FIX: This prints the REAL error to your logs and frontend
            e.printStackTrace();
            return ResponseEntity.status(500).body("Registration Failed: " + e.getMessage());
        }
    }

    // 2. LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        try {
            String email = loginData.get("email");
            String password = loginData.get("password");

            Optional<User> user = userRepository.findByEmail(email);

            // Simple Password Check
            if (user.isPresent() && user.get().getPassword().equals(password)) {
                return ResponseEntity.ok(user.get());
            } else {
                return ResponseEntity.status(401).body("Invalid Credentials");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Login Error: " + e.getMessage());
        }
    }

    // 3. UPDATE PROFILE (Updated with Error Handling)
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody Map<String, String> updates) {
        try {
            Optional<User> userOptional = userRepository.findById(id);

            if (userOptional.isPresent()) {
                User user = userOptional.get();

                // Update Name
                if (updates.containsKey("fullName")) {
                    user.setFullName(updates.get("fullName"));
                }

                // Update Email
                if (updates.containsKey("email")) {
                    user.setEmail(updates.get("email"));
                }

                // Update Password (Only if typed)
                if (updates.containsKey("password") && !updates.get("password").isEmpty()) {
                    user.setPassword(updates.get("password"));
                }

                User updatedUser = userRepository.save(user);
                return ResponseEntity.ok(updatedUser);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Update Failed: " + e.getMessage());
        }
    }
}