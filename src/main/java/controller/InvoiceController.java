package com.codeb.ims.controller;

import com.codeb.ims.dto.InvoiceRequest;
import com.codeb.ims.entity.Invoice;
import com.codeb.ims.service.InvoiceService;
import com.codeb.ims.service.PdfService; // <--- Import PDF Service
import com.codeb.ims.repository.InvoiceRepository; // <--- Import Repository

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource; // <--- For File Download
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.List;

@RestController
@RequestMapping("/api/invoices")
@CrossOrigin(origins = "https://ims-frontend-psi.vercel.app")
public class InvoiceController {

    @Autowired
    private InvoiceService service;

    @Autowired
    private PdfService pdfService; // <--- Inject PDF Logic

    @Autowired
    private InvoiceRepository invoiceRepository; // <--- Inject Repo for quick lookup

    @GetMapping
    public List<Invoice> getAll() {
        return service.getAllInvoices();
    }

    @PostMapping
    public ResponseEntity<?> createInvoice(@RequestBody InvoiceRequest request) {
        try {
            return ResponseEntity.ok(service.createInvoice(request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // --- NEW PDF DOWNLOAD ENDPOINT ---
    @GetMapping(value = "/{id}/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> downloadInvoice(@PathVariable Long id) {
        // 1. Find the invoice
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        // 2. Generate PDF
        ByteArrayInputStream bis = pdfService.generateInvoicePdf(invoice);

        // 3. Set headers so browser downloads it
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=invoice-" + invoice.getInvoiceNumber() + ".pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
}