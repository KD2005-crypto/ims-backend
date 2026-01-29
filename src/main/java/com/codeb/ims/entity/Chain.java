package com.codeb.ims.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // ✅ Stops the crash, keeps the link
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

    private String gstNumber;

    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean isActive = true;

    // ✅ THE CRITICAL FIX
    // We KEEP the link to ClientGroup (so your data stays connected).
    // We only ignore the "chains" list INSIDE the group to stop the infinite echo.
    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    @JsonIgnoreProperties({"chains", "chainList", "clientGroup"})
    @ToString.Exclude // Prevents Lombok from crashing the logs
    private ClientGroup clientGroup;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}