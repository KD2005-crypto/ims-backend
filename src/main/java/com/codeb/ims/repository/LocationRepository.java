package com.codeb.ims.repository;

import com.codeb.ims.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {

    // Existing method (Keep this!)
    List<Location> findByIsActiveTrue();

    // ✅ ADD THIS LINE TO FIX THE RED ERROR
    long countByIsActiveTrue();
}