package com.codeb.ims.repository;

import com.codeb.ims.entity.Chain;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChainRepository extends JpaRepository<Chain, Long> {
    // Find all active chains
    List<Chain> findByIsActiveTrue();

    // Find chains belonging to a specific group (Useful for dropdowns later!)
    List<Chain> findByClientGroup_GroupIdAndIsActiveTrue(Long groupId);
}