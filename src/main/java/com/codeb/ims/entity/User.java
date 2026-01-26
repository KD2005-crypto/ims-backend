package com.codeb.ims.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor; // ✅ Added
import lombok.Data;
import lombok.NoArgsConstructor;  // ✅ Added (Critical for JPA)
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor   // ✅ FIXES CRASH: Required by Hibernate/JPA
@AllArgsConstructor  // ✅ HELPER: Useful for testing
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String password;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private String status = "active";

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.status == null) {
            this.status = "active";
        }
    }
}