package com.codeb.ims.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "estimates")
public class Estimate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long estimatedId;

    // Link to Chain (Company)
    @ManyToOne
    @JoinColumn(name = "chain_id", nullable = false)
    private Chain chain;

    private String groupName;
    private String brandName;
    private String zoneName;
    private String service;

    private int qty;
    private float costPerUnit;

    // --- NEW GST FIELD ---
    private float gstRate; // e.g., 18.0

    private float totalCost; // Calculated: (Qty * Cost) + Tax

    private LocalDate deliveryDate;
    private String deliveryDetails;

    // Auditing
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // --- GETTERS & SETTERS ---
    public Long getEstimatedId() { return estimatedId; }
    public void setEstimatedId(Long estimatedId) { this.estimatedId = estimatedId; }

    public Chain getChain() { return chain; }
    public void setChain(Chain chain) { this.chain = chain; }

    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }

    public String getBrandName() { return brandName; }
    public void setBrandName(String brandName) { this.brandName = brandName; }

    public String getZoneName() { return zoneName; }
    public void setZoneName(String zoneName) { this.zoneName = zoneName; }

    public String getService() { return service; }
    public void setService(String service) { this.service = service; }

    public int getQty() { return qty; }
    public void setQty(int qty) { this.qty = qty; }

    public float getCostPerUnit() { return costPerUnit; }
    public void setCostPerUnit(float costPerUnit) { this.costPerUnit = costPerUnit; }

    public float getGstRate() { return gstRate; }
    public void setGstRate(float gstRate) { this.gstRate = gstRate; }

    public float getTotalCost() { return totalCost; }
    public void setTotalCost(float totalCost) { this.totalCost = totalCost; }

    public LocalDate getDeliveryDate() { return deliveryDate; }
    public void setDeliveryDate(LocalDate deliveryDate) { this.deliveryDate = deliveryDate; }

    public String getDeliveryDetails() { return deliveryDetails; }
    public void setDeliveryDetails(String deliveryDetails) { this.deliveryDetails = deliveryDetails; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}