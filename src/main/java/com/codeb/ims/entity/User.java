package com.codeb.ims.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "users") // Matches PDF Req
@Data
public class User {

    // PDF Req: "user_id" (INT, PRIMARY KEY, AUTO_INCREMENT)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    // PDF Req: "email" (UNIQUE, NOT NULL)
    @Column(unique = true, nullable = false, length = 100)
    private String email;

    // PDF Req: "password_hash" (NOT NULL)
    // We keep the Java field as 'password' for easy coding, but map it to 'password_hash' column
    @Column(name = "password_hash", nullable = false)
    private String password;

    // PDF Req: "full_name" (VARCHAR 100)
    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    // PDF Req: "role" (Defines user access level)
    // Values: "ADMIN" or "SALES"
    @Column(nullable = false)
    private String role;

    // PDF Req: "status" (ENUM 'active', 'inactive')
    // We default it to "active" so new users can login immediately
    @Column(nullable = false)
    private String status = "active";

    // PDF Req: "created_at" (TIMESTAMP)
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Automatically set the timestamp and default status before saving
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