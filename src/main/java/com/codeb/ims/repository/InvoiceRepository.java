package com.codeb.ims.repository;

import com.codeb.ims.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    // --- DELETE ALL OLD METHODS HERE ---
    // Methods like "findByCustomerName" or "findByStatus" MUST GO.
    // They cause the crash because those fields no longer exist.

    // You can add valid ones later, like:
    // Invoice findByInvoiceNo(int invoiceNo);
}