package com.codeb.ims.repository;

import com.codeb.ims.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    // Find all invoices for a specific location (Store)
    List<Invoice> findByLocation_LocationId(Long locationId);

    // Check if invoice number already exists
    boolean existsByInvoiceNumber(String invoiceNumber);
}