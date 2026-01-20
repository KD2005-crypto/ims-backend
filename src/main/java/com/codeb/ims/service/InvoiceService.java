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

    // --- 1. Create Invoice ---
    public Invoice createInvoice(InvoiceRequest request) {
        Invoice invoice = new Invoice();

        int random4Digit = 1000 + new Random().nextInt(9000);
        invoice.setInvoiceNo(random4Digit);

        float amount = (float) request.getAmount();
        invoice.setAmountPayable(amount);
        invoice.setAmountPaid(amount);
        invoice.setBalance(0);

        invoice.setDateOfPayment(LocalDateTime.now());
        invoice.setDateOfService(LocalDate.now());

        invoice.setServiceDetails("Standard Service");
        invoice.setQuantity(1);
        invoice.setCostPerQty(amount);

        return invoiceRepository.save(invoice);
    }

    // --- 2. Get All Invoices (MISSING METHOD ADDED) ---
    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }
}