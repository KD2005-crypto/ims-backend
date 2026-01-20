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

        // 1. Count Brands & Locations
        stats.setTotalBrands(brandRepository.count());
        stats.setTotalLocations(locationRepository.count());

        // 2. Calculate Revenue & Invoices
        List<Invoice> allInvoices = invoiceRepository.findAll();
        stats.setTotalInvoices((long) allInvoices.size());

        // Sum up the "Total Amount" of all invoices
        double revenue = allInvoices.stream()
                .mapToDouble(Invoice::getTotalAmount)
                .sum();
        stats.setTotalRevenue(revenue);

        return stats;
    }
}