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

    private int invoiceNo;
    private Long estimatedId;
    private Long chainId;

    // Bridge fields
    private String clientName;
    private String brandName;
    private String zoneName;

    private String groupName;
    private String serviceDetails;
    private int quantity;
    private float costPerQty;
    private float amountPayable;
    private float amountPaid;
    private float balance;
    private String status;
    private String paymentMode;
    private String transactionId;
    private LocalDate dateOfService;
    private LocalDateTime dateOfPayment;
    private String deliveryDetails;
    private String emailId;

    @Column(name = "is_archived")
    private Boolean archived = false;

    @PrePersist
    protected void onCreate() {
        if (this.status == null) this.status = "PENDING";
        if (this.archived == null) this.archived = false;
    }

    // --- GETTERS & SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public int getInvoiceNo() { return invoiceNo; }
    public void setInvoiceNo(int invoiceNo) { this.invoiceNo = invoiceNo; }
    public Long getEstimatedId() { return estimatedId; }
    public void setEstimatedId(Long estimatedId) { this.estimatedId = estimatedId; }
    public Long getChainId() { return chainId; }
    public void setChainId(Long chainId) { this.chainId = chainId; }

    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }
    public String getBrandName() { return brandName; }
    public void setBrandName(String brandName) { this.brandName = brandName; }
    public String getZoneName() { return zoneName; }
    public void setZoneName(String zoneName) { this.zoneName = zoneName; }

    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }
    public String getServiceDetails() { return serviceDetails; }
    public void setServiceDetails(String serviceDetails) { this.serviceDetails = serviceDetails; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public float getCostPerQty() { return costPerQty; }
    public void setCostPerQty(float costPerQty) { this.costPerQty = costPerQty; }
    public float getAmountPayable() { return amountPayable; }
    public void setAmountPayable(float amountPayable) { this.amountPayable = amountPayable; }

    // ✅ FIXED: Renamed back to standard setAmountPaid
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
    public float getTotalAmount() { return this.amountPayable; }

    public Boolean isArchived() { return archived != null && archived; }
    public void setArchived(Boolean archived) { this.archived = archived; }
}