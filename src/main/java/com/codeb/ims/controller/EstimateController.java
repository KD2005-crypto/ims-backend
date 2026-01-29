package com.codeb.ims.controller;

import com.codeb.ims.dto.EstimateRequest;
import com.codeb.ims.entity.Estimate;
import com.codeb.ims.service.EstimateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estimates")
@CrossOrigin(origins = "*") // Critical for cross-domain communication
public class EstimateController {

    @Autowired
    private EstimateService estimateService;

    @PostMapping
    public ResponseEntity<Estimate> createEstimate(@RequestBody EstimateRequest request) {
        // Sends the request with Group Name to the Service for processing
        return ResponseEntity.ok(estimateService.createEstimate(request));
    }

    @GetMapping
    public ResponseEntity<List<Estimate>> getAllEstimates() {
        // Returns the list containing the chain and group data
        return ResponseEntity.ok(estimateService.getAllEstimates());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEstimate(@PathVariable Long id) {
        estimateService.deleteEstimate(id);
        return ResponseEntity.ok().build();
    }
}