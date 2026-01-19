package com.codeb.ims.repository;

import com.codeb.ims.entity.ChartData;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ChartDataRepository extends JpaRepository<ChartData, Long> {
    Optional<ChartData> findByName(String name);
}