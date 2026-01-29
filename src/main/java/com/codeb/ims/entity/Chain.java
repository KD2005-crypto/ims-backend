package com.codeb.ims.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "chains")
@Data
public class Chain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chainId;

    @Column(nullable = false)
    private String chainName;

    private String gstNumber; // Required for Invoicing

    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean isActive = true;

    // This links the Chain to a Group (Many Chains -> One Group)
    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private ClientGroup clientGroup;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}