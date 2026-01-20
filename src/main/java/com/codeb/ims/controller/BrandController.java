package com.codeb.ims.controller;

import com.codeb.ims.dto.BrandRequest;
import com.codeb.ims.entity.Brand;
import com.codeb.ims.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/brands")
@CrossOrigin(origins = "https://ims-frontend-psi.vercel.app")
public class BrandController {

    @Autowired
    private BrandService service;

    @GetMapping
    public List<Brand> getAll() {
        return service.getAllBrands();
    }

    @PostMapping
    public ResponseEntity<?> addBrand(@RequestBody BrandRequest request) {
        try {
            return ResponseEntity.ok(service.addBrand(request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void deleteBrand(@PathVariable Long id) {
        service.deleteBrand(id);
    }
}