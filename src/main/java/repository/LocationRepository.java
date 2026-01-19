package com.codeb.ims.repository;

import com.codeb.ims.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findByIsActiveTrue();
    // Find locations for a specific brand
    List<Location> findByBrand_BrandIdAndIsActiveTrue(Long brandId);
}