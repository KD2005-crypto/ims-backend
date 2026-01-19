package com.codeb.ims.controller;

import com.codeb.ims.dto.ChainRequest;
import com.codeb.ims.entity.Chain;
import com.codeb.ims.service.ChainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/chains")
@CrossOrigin(origins = "https://ims-frontend-psi.vercel.app")
public class ChainController {

    @Autowired
    private ChainService service;

    @GetMapping
    public List<Chain> getAll() {
        return service.getAllChains();
    }

    @PostMapping
    public ResponseEntity<?> addChain(@RequestBody ChainRequest request) {
        try {
            return ResponseEntity.ok(service.addChain(request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void deleteChain(@PathVariable Long id) {
        service.deleteChain(id);
    }
}