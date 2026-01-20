package com.codeb.ims.dto;

import lombok.Data;

@Data // Generates getters/setters automatically
public class InvoiceRequest {
    private Long estimatedId;

    // --- ADDED FIELD ---
    private String groupName;

    private String serviceDetails;
    private int quantity;
    private float costPerQty;

    // This matches the "amount" field sent by your frontend handleGenerate function
    private double amount;
    private float amountPaid;
    private String emailId;
}