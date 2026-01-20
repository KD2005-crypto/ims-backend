package com.codeb.ims.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Entity
@Table(name = "invoices") // Matches the table name in your PDF
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // [cite: 192] "int(pk) and auto_increment"

    @Column(unique = true, nullable = false)
    private int invoiceNo; // [cite: 192] "Stores four digit unique id"

    private int estimatedId; // [cite: 192] "Stores estimated id"
    private int chainId;     // [cite: 192] "Stores chain id"

    private String serviceDetails; // [cite: 192] "Stores details related to service"
    private int quantity;          // [cite: 192] "Stores quantity"
    private float costPerQty;      // [cite: 192] "Stores cost per quantity"

    private float amountPayable;   // [cite: 192] "Total amount to be paid"
    private float amountPaid;      // [cite: 192]
    private float balance;         // [cite: 192] "Stored balance amount"

    private LocalDateTime dateOfPayment; // [cite: 192] "datetime"
    private LocalDate dateOfService;     // [cite: 192] "date"

    private String deliveryDetails; // [cite: 192] "Address and other delivery details"
    private String emailId;         // [cite: 192] "Stores email id"

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public int getInvoiceNo() { return invoiceNo; }
    public void setInvoiceNo(int invoiceNo) { this.invoiceNo = invoiceNo; }
    public int getEstimatedId() { return estimatedId; }
    public void setEstimatedId(int estimatedId) { this.estimatedId = estimatedId; }
    public int getChainId() { return chainId; }
    public void setChainId(int chainId) { this.chainId = chainId; }
    public String getServiceDetails() { return serviceDetails; }
    public void setServiceDetails(String serviceDetails) { this.serviceDetails = serviceDetails; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public float getCostPerQty() { return costPerQty; }
    public void setCostPerQty(float costPerQty) { this.costPerQty = costPerQty; }
    public float getAmountPayable() { return amountPayable; }
    public void setAmountPayable(float amountPayable) { this.amountPayable = amountPayable; }
    public float getAmountPaid() { return amountPaid; }
    public void setAmountPaid(float amountPaid) { this.amountPaid = amountPaid; }
    public float getBalance() { return balance; }
    public void setBalance(float balance) { this.balance = balance; }
    public LocalDateTime getDateOfPayment() { return dateOfPayment; }
    public void setDateOfPayment(LocalDateTime dateOfPayment) { this.dateOfPayment = dateOfPayment; }
    public LocalDate getDateOfService() { return dateOfService; }
    public void setDateOfService(LocalDate dateOfService) { this.dateOfService = dateOfService; }
    public String getDeliveryDetails() { return deliveryDetails; }
    public void setDeliveryDetails(String deliveryDetails) { this.deliveryDetails = deliveryDetails; }
    public String getEmailId() { return emailId; }
    public void setEmailId(String emailId) { this.emailId = emailId; }
}