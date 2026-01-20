package com.codeb.ims.repository;

import com.codeb.ims.entity.Estimate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EstimateRepository extends JpaRepository<Estimate, Long> {
    // Find all estimates belonging to a specific chain
    List<Estimate> findByChain_ChainId(Long chainId);
}