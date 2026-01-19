package com.codeb.ims.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoices")
@Data
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long invoiceId;

    @Column(unique = true, nullable = false)
    private String invoiceNumber; // e.g., "INV-2024-001"

    @Column(nullable = false)
    private Double amount; // Basic Amount

    @Column(nullable = false)
    private Double taxAmount; // GST (18%)

    @Column(nullable = false)
    private Double totalAmount; // Amount + Tax

    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false)
    private String status; // "PAID", "PENDING", "CANCELLED"

    private String pdfFilePath; // We will store the link to the generated PDF here

    // Link Invoice to a specific Store Location
    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}