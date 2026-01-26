package com.codeb.ims.repository;

import com.codeb.ims.entity.ClientGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ClientGroupRepository extends JpaRepository<ClientGroup, Long> {
    List<ClientGroup> findByIsActiveTrue();
    boolean existsByGroupName(String groupName);

    // ✅ ADD THIS LINE (Essential for the fix)
    Optional<ClientGroup> findByGroupName(String groupName);
}