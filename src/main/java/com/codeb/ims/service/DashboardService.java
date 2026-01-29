package com.codeb.ims.service;

import com.codeb.ims.dto.DashboardStats;
import com.codeb.ims.entity.Invoice;
import com.codeb.ims.repository.BrandRepository;
import com.codeb.ims.repository.InvoiceRepository;
import com.codeb.ims.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardService {

    @Autowired private InvoiceRepository invoiceRepository;
    @Autowired private LocationRepository locationRepository;
    @Autowired private BrandRepository brandRepository;

    public DashboardStats getStats() {
        DashboardStats stats = new DashboardStats();

        // 1. COUNT ACTIVE BRANDS & LOCATIONS
        try {
            stats.setTotalBrands((int) brandRepository.countByIsActiveTrue());
            stats.setTotalLocations((int) locationRepository.countByIsActiveTrue());
        } catch (Exception e) {
            stats.setTotalBrands(0);
            stats.setTotalLocations(0);
        }

        // 2. FETCH ALL INVOICES
        List<Invoice> allInvoices = invoiceRepository.findAll();

        // 3. COUNT ACTIVE INVOICES
        // FIX 1: Changed .getArchived() to .isArchived() (Standard Java convention)
        long activeCount = allInvoices.stream()
                .filter(i -> !Boolean.TRUE.equals(i.isArchived()))
                .count();
        stats.setTotalInvoices(activeCount);

        // 4. SUM REVENUE
        // FIX 2: Removed "!= null" check because 'float' cannot be null.
        // We just grab the value directly.
        double revenue = allInvoices.stream()
                .filter(i -> !Boolean.TRUE.equals(i.isArchived()))
                .mapToDouble(Invoice::getAmountPayable)
                .sum();

        stats.setTotalRevenue(revenue);

        return stats;
    }
}