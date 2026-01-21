package com.codeb.ims.controller; // Keep it in controller package for simplicity

import com.codeb.ims.entity.Invoice;
import com.codeb.ims.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class DataFixer implements CommandLineRunner {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("--- 🛠️ STARTING DATABASE REPAIR ---");

        // 1. Fetch ALL invoices (even broken ones)
        List<Invoice> allInvoices = invoiceRepository.findAll();

        int fixedCount = 0;

        for (Invoice inv : allInvoices) {
            // Check if the new field is NULL
            if (inv.isArchived() == null) {
                inv.setArchived(false); // Set default to Active
                invoiceRepository.save(inv);
                fixedCount++;
            }
        }

        System.out.println("--- ✅ REPAIR COMPLETE: Fixed " + fixedCount + " invoices. ---");
    }
}