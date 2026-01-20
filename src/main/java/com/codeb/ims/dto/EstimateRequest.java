package com.codeb.ims.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class EstimateRequest {
    private Long chainId;
    private String groupName;
    private String brandName;
    private String zoneName;
    private String service;

    private int qty;
    private float costPerUnit;
    private float gstRate; // Input: 18.0

    private LocalDate deliveryDate;
    private String deliveryDetails;
}