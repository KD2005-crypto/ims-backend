package com.codeb.ims.service;

import com.codeb.ims.dto.InvoiceRequest;
import com.codeb.ims.entity.Invoice;
import com.codeb.ims.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.Random;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    public Invoice createInvoice(InvoiceRequest request) {
        Invoice invoice = new Invoice();

        // FIX 1: Generate a 4-digit Number (Matches PDF)
        // Old code used UUID string, which is now illegal for "int invoiceNo"
        int random4Digit = 1000 + new Random().nextInt(9000);
        invoice.setInvoiceNo(random4Digit);

        // FIX 2: Map Amount (Convert double to float)
        float amount = (float) request.getAmount();
        invoice.setAmountPayable(amount);
        invoice.setAmountPaid(amount); // Assume full payment for simplicity
        invoice.setBalance(0);

        // FIX 3: Set Dates
        invoice.setDateOfPayment(LocalDateTime.now());
        invoice.setDateOfService(LocalDate.now());

        // FIX 4: Set Defaults for PDF fields that old Request doesn't have
        invoice.setServiceDetails("Standard Service");
        invoice.setQuantity(1);
        invoice.setCostPerQty(amount);

        // Note: We removed setCustomerName, setStatus, setLocation
        // because the new PDF table does not store them.

        return invoiceRepository.save(invoice);
    }
}