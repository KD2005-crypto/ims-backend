package com.codeb.ims.controller;

import com.codeb.ims.entity.Invoice;
import com.codeb.ims.repository.InvoiceRepository;
import com.codeb.ims.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/invoices")
@CrossOrigin(origins = "*") // Allow Frontend access
public class InvoiceController {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private PdfService pdfService;

    // --- 1. GET ALL INVOICES ---
    @GetMapping
    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    // --- 2. CREATE INVOICE (From Estimate) ---
    @PostMapping("/create")
    public ResponseEntity<Invoice> createInvoice(@RequestBody Map<String, Object> payload) {
        Invoice invoice = new Invoice();

        // Map basic fields
        invoice.setServiceDetails((String) payload.get("serviceDetails"));
        invoice.setEmailId((String) payload.get("emailId"));

        // Handle Numbers (Safely parse from JSON)
        invoice.setQuantity(Integer.parseInt(payload.get("quantity").toString()));
        invoice.setCostPerQty(Float.parseFloat(payload.get("costPerQty").toString()));
        invoice.setAmountPayable(Float.parseFloat(payload.get("amount").toString()));

        // Initial Payment (if any)
        float initialPaid = Float.parseFloat(payload.get("amountPaid").toString());
        invoice.setAmountPaid(initialPaid);
        invoice.setBalance(invoice.getAmountPayable() - initialPaid);

        // Map Linkage IDs (Crucial for tracking)
        if (payload.get("estimatedId") != null) {
            invoice.setEstimatedId(Long.parseLong(payload.get("estimatedId").toString()));
        }

        // Auto-Generate Invoice Number (Simple logic: Count + 1000)
        long count = invoiceRepository.count();
        invoice.setInvoiceNo(1000 + (int) count + 1);

        invoice.setDateOfService(LocalDate.now());

        // Set Status
        if (invoice.getBalance() <= 0) {
            invoice.setStatus("PAID");
        } else {
            invoice.setStatus("PENDING");
        }

        return ResponseEntity.ok(invoiceRepository.save(invoice));
    }

    // --- 3. CONFIRM PAYMENT (The New Feature!) ---
    @PutMapping("/{id}/confirm-payment")
    public ResponseEntity<Invoice> confirmPayment(
            @PathVariable Long id,
            @RequestParam String paymentMode,
            @RequestParam String transactionId,
            @RequestParam float amountPaid) {

        // Find Invoice
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found with ID: " + id));

        // Update Payment Metadata
        invoice.setPaymentMode(paymentMode);
        invoice.setTransactionId(transactionId);

        // Update Financials
        float newTotalPaid = invoice.getAmountPaid() + amountPaid;
        invoice.setAmountPaid(newTotalPaid);

        float newBalance = invoice.getAmountPayable() - newTotalPaid;
        // Prevent negative balance (just in case)
        invoice.setBalance(Math.max(newBalance, 0));

        // Update Status
        if (invoice.getBalance() <= 0) {
            invoice.setStatus("PAID");
        } else {
            invoice.setStatus("PARTIAL");
        }

        // Update Date
        invoice.setDateOfPayment(LocalDateTime.now());

        return ResponseEntity.ok(invoiceRepository.save(invoice));
    }

    // --- 4. DELETE INVOICE ---
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable Long id) {
        invoiceRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    // --- 5. GENERATE PDF ---
    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> generatePdf(@PathVariable Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        ByteArrayInputStream bis = pdfService.createInvoicePdf(invoice);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=invoice_" + invoice.getInvoiceNo() + ".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(bis.readAllBytes());
    }
}