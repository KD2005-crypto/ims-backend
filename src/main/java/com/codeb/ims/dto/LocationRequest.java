package com.codeb.ims.dto;

import lombok.Data;

@Data
public class LocationRequest {
    private String locationName;
    private String address;
    private Long brandId; // The ID of the Brand this location belongs to
}