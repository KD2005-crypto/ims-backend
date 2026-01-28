package com.codeb.ims.controller;

import com.codeb.ims.entity.User;
import com.codeb.ims.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender; // ✅ New: Required for Email Support

    // 1. REGISTER (Existing - Unchanged)
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                return ResponseEntity.badRequest().body("Error: Email already exists");
            }
            if (user.getRole() == null || user.getRole().isEmpty()) {
                user.setRole("STAFF");
            }
            User savedUser = userRepository.save(user);
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Registration Failed: " + e.getMessage());
        }
    }

    // 2. LOGIN (Existing - Unchanged)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        try {
            String email = loginData.get("email");
            String password = loginData.get("password");
            Optional<User> user = userRepository.findByEmail(email);

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

    // 3. FORGOT PASSWORD (✅ New feature)
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            Optional<User> userOptional = userRepository.findByEmail(email);

            if (userOptional.isPresent()) {
                // This link sends the user back to your React frontend
                String resetLink = "https://ims-frontend-psi.vercel.app/authentication/reset-password?email=" + email;

                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(email);
                message.setSubject("Code-B IMS: Password Reset Request");
                message.setText("Hello " + userOptional.get().getFullName() + ",\n\n" +
                        "Click the link below to reset your password:\n" + resetLink +
                        "\n\nIf you did not request this, please ignore this email.");

                mailSender.send(message);
                return ResponseEntity.ok("✅ Reset link sent to your email!");
            }
            return ResponseEntity.status(404).body("❌ Email address not found.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("❌ Error sending email: " + e.getMessage());
        }
    }

    // 4. RESET PASSWORD (✅ New feature)
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String newPassword = request.get("password");

            Optional<User> userOptional = userRepository.findByEmail(email);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                user.setPassword(newPassword);
                userRepository.save(user);
                return ResponseEntity.ok("✅ Password updated successfully!");
            }
            return ResponseEntity.status(404).body("❌ User not found.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("❌ Update failed: " + e.getMessage());
        }
    }

    // 5. UPDATE PROFILE (Existing - Unchanged)
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody Map<String, String> updates) {
        try {
            Optional<User> userOptional = userRepository.findById(id);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                if (updates.containsKey("fullName")) user.setFullName(updates.get("fullName"));
                if (updates.containsKey("email")) user.setEmail(updates.get("email"));
                if (updates.containsKey("password") && !updates.get("password").isEmpty()) {
                    user.setPassword(updates.get("password"));
                }
                return ResponseEntity.ok(userRepository.save(user));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Update Failed: " + e.getMessage());
        }
    }
}