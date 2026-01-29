package com.codeb.ims.dto;

import lombok.Data;

@Data
public class ChainRequest {
    private String chainName;
    private String gstNumber;
    private Long groupId; // We only send the ID from the frontend
}