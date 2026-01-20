package com.codeb.ims.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoices")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- IDENTIFIERS (The "Clear Management" columns you asked for) ---
    private int invoiceNo;      // The final Invoice Number (e.g., 1001)
    private Long estimatedId;   // Links back to the Estimate
    private Long chainId;       // Links to the Client

    // --- DETAILS ---
    private String serviceDetails;
    private int quantity;
    private float costPerQty;

    // --- FINANCIALS ---
    private float amountPayable; // Total Amount
    private float amountPaid;    // How much received so far
    private float balance;       // Remaining (Total - Paid)

    // --- NEW: PAYMENT LIFECYCLE FIELDS ---
    private String status;        // "PENDING", "PAID", "PARTIAL"
    private String paymentMode;   // "UPI", "NEFT", "CASH", "CHEQUE"
    private String transactionId; // "UPI-123456" or Cheque No.

    // --- DATES ---
    private LocalDate dateOfService;
    private LocalDateTime dateOfPayment; // When was the LAST payment made?
    private String deliveryDetails;
    private String emailId;

    // --- AUTO STATUS SETTER ---
    @PrePersist
    protected void onCreate() {
        if (this.status == null) {
            this.status = "PENDING"; // Default status
        }
    }

    // --- GETTERS AND SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public int getInvoiceNo() { return invoiceNo; }
    public void setInvoiceNo(int invoiceNo) { this.invoiceNo = invoiceNo; }

    public Long getEstimatedId() { return estimatedId; }
    public void setEstimatedId(Long estimatedId) { this.estimatedId = estimatedId; }

    public Long getChainId() { return chainId; }
    public void setChainId(Long chainId) { this.chainId = chainId; }

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

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPaymentMode() { return paymentMode; }
    public void setPaymentMode(String paymentMode) { this.paymentMode = paymentMode; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public LocalDate getDateOfService() { return dateOfService; }
    public void setDateOfService(LocalDate dateOfService) { this.dateOfService = dateOfService; }

    public LocalDateTime getDateOfPayment() { return dateOfPayment; }
    public void setDateOfPayment(LocalDateTime dateOfPayment) { this.dateOfPayment = dateOfPayment; }

    public String getDeliveryDetails() { return deliveryDetails; }
    public void setDeliveryDetails(String deliveryDetails) { this.deliveryDetails = deliveryDetails; }

    public String getEmailId() { return emailId; }
    public void setEmailId(String emailId) { this.emailId = emailId; }

    // Helper for PdfService
    public float getTotalAmount() { return this.amountPayable; }
}