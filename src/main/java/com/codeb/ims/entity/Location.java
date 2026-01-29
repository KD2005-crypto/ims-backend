package com.codeb.ims.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "locations")
@Data
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long locationId;

    @Column(nullable = false)
    private String locationName; // e.g., "Bandra West Store"

    private String address; // e.g., "Shop 5, Linking Road"

    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean isActive = true;

    // Link Location to a Brand (Many Locations -> One Brand)
    @ManyToOne
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}