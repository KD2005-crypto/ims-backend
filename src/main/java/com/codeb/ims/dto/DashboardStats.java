package com.codeb.ims.dto;

public class DashboardStats {
    private int totalBrands;
    private int totalLocations;
    private long totalInvoices;
    private double totalRevenue;

    // Getters
    public int getTotalBrands() { return totalBrands; }
    public int getTotalLocations() { return totalLocations; }
    public long getTotalInvoices() { return totalInvoices; }
    public double getTotalRevenue() { return totalRevenue; }

    // Setters
    public void setTotalBrands(int totalBrands) { this.totalBrands = totalBrands; }
    public void setTotalLocations(int totalLocations) { this.totalLocations = totalLocations; }
    public void setTotalInvoices(long totalInvoices) { this.totalInvoices = totalInvoices; }
    public void setTotalRevenue(double totalRevenue) { this.totalRevenue = totalRevenue; }
}