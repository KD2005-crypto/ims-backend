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

    // --- 1. GET ALL INVOICES ---
    @GetMapping
    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    // --- 2. CREATE INVOICE ---
    @PostMapping("/create")
    public ResponseEntity<Invoice> createInvoice(@RequestBody Map<String, Object> payload) {
        Invoice invoice = new Invoice();
        invoice.setServiceDetails((String) payload.get("serviceDetails"));
        invoice.setEmailId((String) payload.get("emailId"));
        invoice.setQuantity(Integer.parseInt(payload.get("quantity").toString()));
        invoice.setCostPerQty(Float.parseFloat(payload.get("costPerQty").toString()));
        invoice.setAmountPayable(Float.parseFloat(payload.get("amount").toString()));

        float initialPaid = Float.parseFloat(payload.get("amountPaid").toString());
        invoice.setAmountPaid(initialPaid);
        invoice.setBalance(invoice.getAmountPayable() - initialPaid);

        if (payload.get("estimatedId") != null) {
            invoice.setEstimatedId(Long.parseLong(payload.get("estimatedId").toString()));
        }

        long count = invoiceRepository.count();
        invoice.setInvoiceNo(1000 + (int) count + 1);
        invoice.setDateOfService(LocalDate.now());

        // Default Status
        invoice.setStatus(invoice.getBalance() <= 0 ? "PAID" : "PENDING");

        // REMOVED: invoice.setArchived(false) - This line was causing the crash

        return ResponseEntity.ok(invoiceRepository.save(invoice));
    }

    // --- 3. CONFIRM PAYMENT ---
    @PutMapping("/{id}/confirm-payment")
    public ResponseEntity<Invoice> confirmPayment(
            @PathVariable Long id,
            @RequestBody Map<String, Object> paymentDetails) {

        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found with ID: " + id));

        String paymentMode = (String) paymentDetails.get("paymentMode");
        String transactionId = (String) paymentDetails.get("transactionId");

        float amountPaidNow = invoice.getBalance();

        invoice.setPaymentMode(paymentMode);
        invoice.setTransactionId(transactionId);
        invoice.setAmountPaid(invoice.getAmountPaid() + amountPaidNow);
        invoice.setBalance(0);
        invoice.setStatus("PAID");
        invoice.setDateOfPayment(LocalDateTime.now());

        return ResponseEntity.ok(invoiceRepository.save(invoice));
    }

    // --- REMOVED: ARCHIVE & RESTORE ENDPOINTS (To fix 500 Error) ---

    // --- 4. PERMANENT DELETE ---
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable Long id) {
        invoiceRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    // --- 5. PDF ---
    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> generatePdf(@PathVariable Long id) {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow();
        ByteArrayInputStream bis = pdfService.createInvoicePdf(invoice);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=invoice_" + invoice.getInvoiceNo() + ".pdf");
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(bis.readAllBytes());
    }
}