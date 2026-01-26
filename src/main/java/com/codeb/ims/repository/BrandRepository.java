package com.codeb.ims.repository;

import com.codeb.ims.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BrandRepository extends JpaRepository<Brand, Long> {

    // ✅ 1. USED BY BRAND SERVICE (Fixes the error in your screenshot)
    List<Brand> findByIsActiveTrue();

    // ✅ 2. USED FOR FILTERING CHAINS (Keep this!)
    List<Brand> findByChain_ChainIdAndIsActiveTrue(Long chainId);

    // ✅ 3. USED BY DASHBOARD SERVICE (Fixes the "0" or wrong count on Dashboard)
    long countByIsActiveTrue();
}