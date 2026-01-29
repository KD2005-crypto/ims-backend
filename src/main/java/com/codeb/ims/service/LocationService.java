package com.codeb.ims.service;

import com.codeb.ims.dto.LocationRequest;
import com.codeb.ims.entity.Brand;
import com.codeb.ims.entity.Location;
import com.codeb.ims.repository.BrandRepository;
import com.codeb.ims.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private BrandRepository brandRepository;

    public List<Location> getAllLocations() {
        return locationRepository.findByIsActiveTrue();
    }

    public Location addLocation(LocationRequest request) {
        // 1. Find the Brand first
        Brand brand = brandRepository.findById(request.getBrandId())
                .orElseThrow(() -> new RuntimeException("Brand not found!"));

        // 2. Create the Location
        Location location = new Location();
        location.setLocationName(request.getLocationName());
        location.setAddress(request.getAddress());
        location.setBrand(brand);

        return locationRepository.save(location);
    }

    public void deleteLocation(Long id) {
        Location location = locationRepository.findById(id).orElseThrow();
        location.setActive(false);
        locationRepository.save(location);
    }
}