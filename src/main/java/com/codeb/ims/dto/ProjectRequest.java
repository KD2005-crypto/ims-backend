package com.codeb.ims.dto;

import lombok.Data;
import java.util.List;

@Data
public class ProjectRequest {
    private String name;
    private String budget;
    private int completion;      // Progress (0-100)
    private List<Long> memberIds; // List of IDs (e.g., [1, 2, 4])
}