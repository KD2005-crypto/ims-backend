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
        // 1. Validate Chain
        Chain chain = chainRepository.findById(request.getChainId())
                .orElseThrow(() -> new RuntimeException("Chain not found ID: " + request.getChainId()));

        // 2. Map Data
        Estimate estimate = new Estimate();
        estimate.setChain(chain);
        estimate.setGroupName(request.getGroupName());
        estimate.setBrandName(request.getBrandName());
        estimate.setZoneName(request.getZoneName());
        estimate.setService(request.getService());
        estimate.setDeliveryDate(request.getDeliveryDate());
        estimate.setDeliveryDetails(request.getDeliveryDetails());

        estimate.setQty(request.getQty());
        estimate.setCostPerUnit(request.getCostPerUnit());
        estimate.setGstRate(request.getGstRate());

        // 3. CALCULATE TOTAL (Base + GST)
        float baseAmount = request.getQty() * request.getCostPerUnit();
        float taxAmount = baseAmount * (request.getGstRate() / 100);
        float finalTotal = baseAmount + taxAmount;

        estimate.setTotalCost(finalTotal);

        return estimateRepository.save(estimate);
    }

    public List<Estimate> getAllEstimates() {
        return estimateRepository.findAll();
    }

    public void deleteEstimate(Long id) {
        estimateRepository.deleteById(id);
    }
}