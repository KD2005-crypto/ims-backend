package com.codeb.ims.dto;

public class InvoiceRequest {

    // The frontend sends "amount" (JSON), so we need this field
    private double amount;

    // We can keep these just in case, or add more as needed
    private String customerName;
    private String email;

    // --- GETTERS AND SETTERS ---
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}