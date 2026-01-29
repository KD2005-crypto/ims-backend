package com.codeb.ims.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "leaves")
public class LeaveRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String reason;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status = "PENDING"; // PENDING, APPROVED, REJECTED
}