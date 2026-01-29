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
@CrossOrigin(origins = "*")
public class InvoiceController {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private PdfService pdfService;

    @GetMapping
    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    @PostMapping("/create")
    public ResponseEntity<Invoice> createInvoice(@RequestBody Map<String, Object> payload) {
        Invoice invoice = new Invoice();
        // Handle potential nulls safely
        invoice.setServiceDetails(payload.get("serviceDetails") != null ? (String) payload.get("serviceDetails") : "");
        invoice.setEmailId(payload.get("emailId") != null ? (String) payload.get("emailId") : "");

        // Parse Numbers Safely
        invoice.setQuantity(Integer.parseInt(payload.getOrDefault("quantity", "0").toString()));
        invoice.setCostPerQty(Float.parseFloat(payload.getOrDefault("costPerQty", "0").toString()));
        invoice.setAmountPayable(Float.parseFloat(payload.getOrDefault("amount", "0").toString()));

        // Initial Payment Logic
        float initialPaid = 0;
        if (payload.get("amountPaid") != null) {
            initialPaid = Float.parseFloat(payload.get("amountPaid").toString());
        }
        invoice.setAmountPaid(initialPaid);
        invoice.setBalance(invoice.getAmountPayable() - initialPaid);

        if (payload.get("estimatedId") != null) {
            invoice.setEstimatedId(Long.parseLong(payload.get("estimatedId").toString()));
        }

        long count = invoiceRepository.count();
        invoice.setInvoiceNo(1000 + (int) count + 1);
        invoice.setDateOfService(LocalDate.now());

        // Determine Initial Status
        invoice.setStatus(invoice.getBalance() <= 0 ? "PAID" : "PENDING");
        invoice.setArchived(false); // Active by default

        return ResponseEntity.ok(invoiceRepository.save(invoice));
    }

    // --- UPDATED: PARTIAL PAYMENT LOGIC ---
    @PutMapping("/{id}/confirm-payment")
    public ResponseEntity<Invoice> confirmPayment(@PathVariable Long id, @RequestBody Map<String, Object> paymentDetails) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + id));

        // 1. Get the NEW Amount being paid right now
        float newPayment = 0;
        if (paymentDetails.get("amount") != null) {
            newPayment = Float.parseFloat(paymentDetails.get("amount").toString());
        }

        // 2. Get what was PREVIOUSLY paid
        float previouslyPaid = invoice.getAmountPaid(); // Assuming primitive float, or check null if Float

        // 3. Calculate CUMULATIVE Total
        float totalPaidSoFar = previouslyPaid + newPayment;
        invoice.setAmountPaid(totalPaidSoFar);

        // 4. Update Balance
        float totalCost = invoice.getAmountPayable();
        float newBalance = totalCost - totalPaidSoFar;

        // Safety: Don't let balance go negative
        if (newBalance < 0) newBalance = 0;
        invoice.setBalance(newBalance);

        // 5. Update Payment Metadata
        if (paymentDetails.get("paymentMode") != null) {
            invoice.setPaymentMode((String) paymentDetails.get("paymentMode"));
        }
        if (paymentDetails.get("transactionId") != null) {
            // Append transaction ID if one already exists, or just set it
            String newTxn = (String) paymentDetails.get("transactionId");
            String oldTxn = invoice.getTransactionId();
            if (oldTxn != null && !oldTxn.isEmpty()) {
                invoice.setTransactionId(oldTxn + ", " + newTxn);
            } else {
                invoice.setTransactionId(newTxn);
            }
        }

        // 6. SMART STATUS UPDATE
        // Only mark PAID if balance is zero (or negligible floating point diff)
        if (newBalance <= 0.1) {
            invoice.setStatus("PAID");
            invoice.setDateOfPayment(LocalDateTime.now()); // Set completion date
        } else {
            // If they paid something but not all, keep it PENDING (Frontend handles "Partial" badge)
            invoice.setStatus("PENDING");
        }

        return ResponseEntity.ok(invoiceRepository.save(invoice));
    }

    // --- RESTORED: Archive Endpoint ---
    @PutMapping("/{id}/archive")
    public ResponseEntity<Invoice> archiveInvoice(@PathVariable Long id) {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow();
        invoice.setArchived(true);
        return ResponseEntity.ok(invoiceRepository.save(invoice));
    }

    // --- RESTORED: Restore Endpoint ---
    @PutMapping("/{id}/restore")
    public ResponseEntity<Invoice> restoreInvoice(@PathVariable Long id) {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow();
        invoice.setArchived(false);
        return ResponseEntity.ok(invoiceRepository.save(invoice));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable Long id) {
        invoiceRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> generatePdf(@PathVariable Long id) {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow();
        ByteArrayInputStream bis = pdfService.createInvoicePdf(invoice);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=invoice_" + invoice.getInvoiceNo() + ".pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(bis.readAllBytes());
    }
}