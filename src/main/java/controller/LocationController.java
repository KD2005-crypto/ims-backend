package com.codeb.ims.controller;

import com.codeb.ims.dto.LocationRequest;
import com.codeb.ims.entity.Location;
import com.codeb.ims.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/locations")
@CrossOrigin(origins = "https://ims-frontend-psi.vercel.app")
public class LocationController {

    @Autowired
    private LocationService service;

    @GetMapping
    public List<Location> getAll() {
        return service.getAllLocations();
    }

    @PostMapping
    public ResponseEntity<?> addLocation(@RequestBody LocationRequest request) {
        try {
            return ResponseEntity.ok(service.addLocation(request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void deleteLocation(@PathVariable Long id) {
        service.deleteLocation(id);
    }
}