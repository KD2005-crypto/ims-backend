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
        invoice.setServiceDetails((String) payload.get("serviceDetails"));
        invoice.setEmailId((String) payload.get("emailId"));
        invoice.setQuantity(Integer.parseInt(payload.get("quantity").toString()));
        invoice.setCostPerQty(Float.parseFloat(payload.get("costPerQty").toString()));
        invoice.setAmountPayable(Float.parseFloat(payload.get("amount").toString()));

        float initialPaid = Float.parseFloat(payload.get("amountPaid").toString());
        invoice.setAmountPaid(initialPaid);
        invoice.setBalance(invoice.getAmountPayable() - initialPaid);

        if (payload.get("estimatedId") != null) invoice.setEstimatedId(Long.parseLong(payload.get("estimatedId").toString()));

        long count = invoiceRepository.count();
        invoice.setInvoiceNo(1000 + (int) count + 1);
        invoice.setDateOfService(LocalDate.now());
        invoice.setStatus(invoice.getBalance() <= 0 ? "PAID" : "PENDING");
        invoice.setArchived(false); // Active by default

        return ResponseEntity.ok(invoiceRepository.save(invoice));
    }

    @PutMapping("/{id}/confirm-payment")
    public ResponseEntity<Invoice> confirmPayment(@PathVariable Long id, @RequestBody Map<String, Object> paymentDetails) {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow();
        String paymentMode = (String) paymentDetails.get("paymentMode");
        String transactionId = (String) paymentDetails.get("transactionId");

        invoice.setPaymentMode(paymentMode);
        invoice.setTransactionId(transactionId);
        invoice.setAmountPaid(invoice.getAmountPaid() + invoice.getBalance());
        invoice.setBalance(0);
        invoice.setStatus("PAID");
        invoice.setDateOfPayment(LocalDateTime.now());
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