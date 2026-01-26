package com.codeb.ims.repository;

import com.codeb.ims.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BrandRepository extends JpaRepository<Brand, Long> {

    // Existing method (Keep this!)
    List<Brand> findByIsActiveTrue();

    // Existing Filter (Keep this!)
    List<Brand> findByChain_ChainIdAndIsActiveTrue(Long chainId);

    // ✅ ADD THIS LINE TO FIX THE RED ERROR
    long countByIsActiveTrue();
}