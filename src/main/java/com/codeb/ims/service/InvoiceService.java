package com.codeb.ims.service;

import com.codeb.ims.dto.InvoiceRequest;
import com.codeb.ims.entity.Invoice;
import com.codeb.ims.entity.Estimate; // ✅ FIXED: Changed from SalesEstimate to Estimate
import com.codeb.ims.repository.InvoiceRepository;
import com.codeb.ims.repository.EstimateRepository;
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

    @Autowired
    private EstimateRepository estimateRepository;

    public Invoice createInvoice(InvoiceRequest request) {
        Invoice invoice = new Invoice();

        // 1. Generate unique Invoice Number
        int random4Digit = 1000 + new Random().nextInt(9000);
        invoice.setInvoiceNo(random4Digit);

        // 2. FETCH ESTIMATE DATA (The Bridge)
        // This looks up the original Estimate to get Client and Zone details
        if (request.getEstimatedId() != null) {
            estimateRepository.findById(request.getEstimatedId()).ifPresent((Estimate estimate) -> {
                // ✅ Copy the "Rich" data from Estimate to Invoice
                invoice.setEstimatedId(estimate.getEstimatedId());

                // Fetch Client Name from the Chain relationship if it exists
                if (estimate.getChain() != null) {
                    invoice.setClientName(estimate.getChain().getChainName());
                    invoice.setChainId(estimate.getChain().getChainId());
                } else {
                    invoice.setClientName("Walk-in Client");
                }

                invoice.setBrandName(estimate.getBrandName());
                invoice.setZoneName(estimate.getZoneName());
                invoice.setGroupName(estimate.getGroupName());
            });
        }

        // 3. Map standard fields from Request
        invoice.setServiceDetails(request.getServiceDetails());
        invoice.setQuantity(request.getQuantity());
        invoice.setCostPerQty(request.getCostPerQty());
        invoice.setEmailId(request.getEmailId());

        // 4. Financial Mapping
        float amount = (float) request.getAmount();
        invoice.setAmountPayable(amount);
        invoice.setAmountPaid(request.getAmountPaid());
        invoice.setBalance(amount - request.getAmountPaid());

        // 5. Status Logic
        if (invoice.getBalance() <= 0) {
            invoice.setStatus("PAID");
        } else if (invoice.getAmountPaid() > 0) {
            invoice.setStatus("PARTIAL");
        } else {
            invoice.setStatus("PENDING");
        }

        invoice.setDateOfPayment(LocalDateTime.now());
        invoice.setDateOfService(LocalDate.now());
        invoice.setArchived(false);

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