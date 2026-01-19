package com.codeb.ims.dto;

import lombok.Data;

@Data
public class InvoiceRequest {
    private String customerName;
    private Double amount;
    private Long locationId; // Which store is generating this invoice?
}