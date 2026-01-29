package com.codeb.ims.dto;

import lombok.Data;

@Data // Generates getters/setters automatically
public class InvoiceRequest {
    private Long estimatedId;

    // --- ENHANCED FIELDS ---
    private String groupName;
    private String clientName; // ✅ Added to capture Client from Frontend
    private String zoneName;   // ✅ Added to capture Location/Zone from Frontend

    private String serviceDetails;
    private int quantity;
    private float costPerQty;

    // This matches the "amount" field sent by your frontend handleGenerate function
    private double amount;
    private float amountPaid;
    private String emailId;
}