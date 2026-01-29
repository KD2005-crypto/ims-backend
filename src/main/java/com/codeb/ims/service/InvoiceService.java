package com.codeb.ims.service;

import com.codeb.ims.dto.InvoiceRequest;
import com.codeb.ims.entity.Invoice;
import com.codeb.ims.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    public Invoice createInvoice(InvoiceRequest request) {
        Invoice invoice = new Invoice();

        // 1. Generate unique Invoice Number
        int random4Digit = 1000 + new Random().nextInt(9000);
        invoice.setInvoiceNo(random4Digit);

        // 2. Map standard fields from Request
        invoice.setEstimatedId(request.getEstimatedId());

        // --- CRITICAL FIX: Mapping the new fields ---
        invoice.setGroupName(request.getGroupName());
        invoice.setServiceDetails(request.getServiceDetails());
        invoice.setQuantity(request.getQuantity());
        invoice.setCostPerQty(request.getCostPerQty());
        invoice.setEmailId(request.getEmailId());

        // 3. Financial Mapping
        float amount = (float) request.getAmount();
        invoice.setAmountPayable(amount);
        invoice.setAmountPaid(request.getAmountPaid());
        invoice.setBalance(amount - request.getAmountPaid());

        // 4. Status Logic
        if (invoice.getBalance() <= 0) {
            invoice.setStatus("PAID");
        } else if (invoice.getAmountPaid() > 0) {
            invoice.setStatus("PARTIAL");
        } else {
            invoice.setStatus("PENDING");
        }

        invoice.setDateOfPayment(LocalDateTime.now());
        invoice.setDateOfService(LocalDate.now());

        return invoiceRepository.save(invoice);
    }

    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    public Invoice getInvoiceById(Long id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + id));
    }
}