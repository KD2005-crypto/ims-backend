package com.codeb.ims.controller;

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

        List<Invoice> allInvoices = invoiceRepository.findAll();
        int fixedCount = 0;

        for (Invoice inv : allInvoices) {
            // AGGRESSIVE FIX:
            // Don't ask "is it null?". Just force it to FALSE (Active) if it's not TRUE.
            // This ensures every single old invoice gets a valid value.
            if (!Boolean.TRUE.equals(inv.isArchived())) {
                inv.setArchived(false);
                invoiceRepository.save(inv);
                fixedCount++;
            }
        }

        System.out.println("--- ✅ REPAIR COMPLETE: Updated " + fixedCount + " invoices. ---");
    }
}