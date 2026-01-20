package com.codeb.ims.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "chart_data")
@Data
public class ChartData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name; // e.g., "weekly_sales", "monthly_growth"

    private String description; // e.g., "Last Campaign Performance"

    // We will store arrays as comma-separated strings for simplicity
    // e.g., "M,T,W,T,F,S,S"
    private String labels;

    // e.g., "50,20,10,22,50,10,40"
    private String dataPoints;

    private String updateTime; // e.g., "updated 4 min ago"
}