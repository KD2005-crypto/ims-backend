package com.codeb.ims.controller;

import com.codeb.ims.entity.Invoice;
import com.codeb.ims.repository.InvoiceRepository;
import com.codeb.ims.service.InvoiceService; // ✅ Import the service
import com.codeb.ims.service.PdfService;
import com.codeb.ims.dto.InvoiceRequest; // ✅ Import the DTO we used in service
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/invoices")
@CrossOrigin(origins = "*")
public class InvoiceController {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private InvoiceService invoiceService; // ✅ Inject the fixed service

    @Autowired
    private PdfService pdfService;

    @GetMapping
    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    // ✅ FIXED: Now uses the Service to bridge data from Estimates
    @PostMapping("/create")
    public ResponseEntity<Invoice> createInvoice(@RequestBody InvoiceRequest request) {
        // This now calls the logic that finds the Estimate, grabs the Client Name,
        // grabs the Zone, and saves it all into the Invoice
        Invoice savedInvoice = invoiceService.createInvoice(request);
        return ResponseEntity.ok(savedInvoice);
    }

    @PutMapping("/{id}/confirm-payment")
    public ResponseEntity<Invoice> confirmPayment(@PathVariable Long id, @RequestBody Map<String, Object> paymentDetails) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + id));

        float newPayment = 0;
        if (paymentDetails.get("amount") != null) {
            newPayment = Float.parseFloat(paymentDetails.get("amount").toString());
        }

        float previouslyPaid = invoice.getAmountPaid();
        float totalPaidSoFar = previouslyPaid + newPayment;
        invoice.setAmountPaid(totalPaidSoFar);

        float totalCost = invoice.getAmountPayable();
        float newBalance = totalCost - totalPaidSoFar;

        if (newBalance < 0) newBalance = 0;
        invoice.setBalance(newBalance);

        if (paymentDetails.get("paymentMode") != null) {
            invoice.setPaymentMode((String) paymentDetails.get("paymentMode"));
        }
        if (paymentDetails.get("transactionId") != null) {
            String newTxn = (String) paymentDetails.get("transactionId");
            String oldTxn = invoice.getTransactionId();
            if (oldTxn != null && !oldTxn.isEmpty()) {
                invoice.setTransactionId(oldTxn + ", " + newTxn);
            } else {
                invoice.setTransactionId(newTxn);
            }
        }

        if (newBalance <= 0.1) {
            invoice.setStatus("PAID");
            invoice.setDateOfPayment(java.time.LocalDateTime.now());
        } else {
            invoice.setStatus("PENDING");
        }

        return ResponseEntity.ok(invoiceRepository.save(invoice));
    }

    @PutMapping("/{id}/archive")
    public ResponseEntity<Invoice> archiveInvoice(@PathVariable Long id) {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow();
        invoice.setArchived(true);
        return ResponseEntity.ok(invoiceRepository.save(invoice));
    }

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