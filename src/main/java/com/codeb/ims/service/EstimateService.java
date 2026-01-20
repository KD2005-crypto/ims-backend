package com.codeb.ims.service;

import com.codeb.ims.dto.EstimateRequest;
import com.codeb.ims.entity.Chain;
import com.codeb.ims.entity.Estimate;
import com.codeb.ims.repository.ChainRepository;
import com.codeb.ims.repository.EstimateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EstimateService {

    @Autowired
    private EstimateRepository estimateRepository;

    @Autowired
    private ChainRepository chainRepository;

    public Estimate createEstimate(EstimateRequest request) {
        Chain chain = chainRepository.findById(request.getChainId())
                .orElseThrow(() -> new RuntimeException("Chain not found ID: " + request.getChainId()));

        Estimate estimate = new Estimate();
        estimate.setChain(chain);

        // --- DATA MAPPING FIXES ---
        estimate.setGroupName(request.getGroupName()); // Crucial: Saves the Group Name
        estimate.setBrandName(request.getBrandName());
        estimate.setZoneName(request.getZoneName());
        estimate.setService(request.getService());
        estimate.setQty(request.getQty());
        estimate.setCostPerUnit(request.getCostPerUnit());
        estimate.setGstRate(request.getGstRate());
        estimate.setDeliveryDate(request.getDeliveryDate());
        estimate.setDeliveryDetails(request.getDeliveryDetails());

        // Calculation logic
        float baseAmount = request.getQty() * request.getCostPerUnit();
        float taxAmount = baseAmount * (request.getGstRate() / 100);
        estimate.setTotalCost(baseAmount + taxAmount);

        return estimateRepository.save(estimate);
    }

    public List<Estimate> getAllEstimates() {
        return estimateRepository.findAll();
    }

    public void deleteEstimate(Long id) {
        estimateRepository.deleteById(id);
    }
}