package com.codeb.ims.service;

import com.codeb.ims.dto.InvoiceRequest;
import com.codeb.ims.entity.Invoice;
import com.codeb.ims.entity.Location;
import com.codeb.ims.repository.InvoiceRepository;
import com.codeb.ims.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private LocationRepository locationRepository;

    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    public Invoice createInvoice(InvoiceRequest request) {
        // 1. Find the Location (Store)
        Location location = locationRepository.findById(request.getLocationId())
                .orElseThrow(() -> new RuntimeException("Store Location not found!"));

        // 2. Calculate Taxes (18% GST)
        double basicAmount = request.getAmount();
        double tax = basicAmount * 0.18; // 18% calculation
        double total = basicAmount + tax;

        // 3. Create the Invoice Object
        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber("INV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase()); // Generate unique ID
        invoice.setCustomerName(request.getCustomerName());
        invoice.setAmount(basicAmount);
        invoice.setTaxAmount(tax);
        invoice.setTotalAmount(total);
        invoice.setStatus("PAID");
        invoice.setLocation(location);

        // (Later we will add PDF generation here!)

        return invoiceRepository.save(invoice);
    }
}