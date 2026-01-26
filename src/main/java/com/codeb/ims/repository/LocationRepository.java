package com.codeb.ims.repository;

import com.codeb.ims.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {

    // Keeps the Location List working
    List<Location> findByIsActiveTrue();

    // Keeps the Dashboard Count working
    long countByIsActiveTrue();
}