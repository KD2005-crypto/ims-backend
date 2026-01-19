package com.codeb.ims.dto;

import lombok.Data;

@Data
public class BrandRequest {
    private String brandName;
    private Long chainId; // The ID of the Chain this brand belongs to
}