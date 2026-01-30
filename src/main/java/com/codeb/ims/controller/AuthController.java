package com.codeb.ims.controller;

import com.codeb.ims.entity.User;
import com.codeb.ims.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // Allows your frontend to connect
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    // Load API Key from Railway Variables
    @Value("${codeb.brevo.apikey}")
    private String brevoApiKey;

    @Value("${codeb.brevo.sender}")
    private String senderEmail;

    // --- 1. REGISTER (Public - Creates STAFF by default) ---
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                return ResponseEntity.badRequest().body("Error: Email already exists");
            }
            // Default role is always STAFF for public registration
            if (user.getRole() == null || user.getRole().isEmpty()) {
                user.setRole("STAFF");
            }
            return ResponseEntity.ok(userRepository.save(user));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    // --- 2. LOGIN ---
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        try {
            String email = loginData.get("email");
            String password = loginData.get("password");
            Optional<User> user = userRepository.findByEmail(email);

            if (user.isPresent() && user.get().getPassword().equals(password)) {
                return ResponseEntity.ok(user.get());
            }
            return ResponseEntity.status(401).body("Invalid Credentials");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    // --- 3. FORGOT PASSWORD ---
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            try {
                // Ensure this matches your Vercel URL
                String resetLink = "https://ims-frontend-psi.vercel.app/authentication/reset-password?email=" + email;

                String htmlContent = "<html><body>" +
                        "<h3>Code-B IMS Password Reset</h3>" +
                        "<p>Hello " + userOptional.get().getFullName() + ",</p>" +
                        "<p>Click the link below to reset your password:</p>" +
                        "<a href='" + resetLink + "'>Reset Password Here</a>" +
                        "<p>If you did not request this, ignore this email.</p>" +
                        "</body></html>";

                Map<String, Object> body = new HashMap<>();
                Map<String, String> sender = new HashMap<>();
                sender.put("name", "Code-B IMS");
                sender.put("email", senderEmail);
                body.put("sender", sender);

                List<Map<String, String>> toList = new ArrayList<>();
                Map<String, String> to = new HashMap<>();
                to.put("email", email);
                toList.add(to);
                body.put("to", toList);

                body.put("subject", "Reset Your Password");
                body.put("htmlContent", htmlContent);

                String apiUrl = "https://api.brevo.com/v3/smtp/email";
                HttpHeaders headers = new HttpHeaders();
                headers.set("api-key", brevoApiKey);
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
                RestTemplate restTemplate = new RestTemplate();

                ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, entity, String.class);

                if (response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK) {
                    return ResponseEntity.ok("✅ Reset link sent successfully!");
                } else {
                    return ResponseEntity.status(500).body("❌ Email failed: " + response.getBody());
                }

            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("❌ Error: " + e.getMessage());
            }
        }
        return ResponseEntity.status(404).body("❌ Email not found.");
    }

    // --- 4. RESET PASSWORD ---
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
                return ResponseEntity.ok("✅ Password updated!");
            }
            return ResponseEntity.status(404).body("❌ User not found.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("❌ Error: " + e.getMessage());
        }
    }

    // --- 5. UPDATE PROFILE ---
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody Map<String, String> updates) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (updates.containsKey("fullName")) user.setFullName(updates.get("fullName"));
            if (updates.containsKey("email")) user.setEmail(updates.get("email"));
            if (updates.containsKey("password") && !updates.get("password").isEmpty()) {
                user.setPassword(updates.get("password"));
            }
            return ResponseEntity.ok(userRepository.save(user));
        }
        return ResponseEntity.notFound().build();
    }

    // --- 6. CREATE NEW ADMIN (Protected Endpoint) ---
    // ✅ This allows an existing Admin to create another Admin via Settings
    @PostMapping("/create-admin")
    public ResponseEntity<?> createAdmin(@RequestBody User user) {
        try {
            // Check if email exists
            if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                return ResponseEntity.badRequest().body("Error: Email already exists");
            }

            // FORCE Role to ADMIN
            user.setRole("ADMIN");
            user.setStatus("active");

            return ResponseEntity.ok(userRepository.save(user));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}