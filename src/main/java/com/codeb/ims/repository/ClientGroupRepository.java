package com.codeb.ims.repository;

import com.codeb.ims.entity.ClientGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ClientGroupRepository extends JpaRepository<ClientGroup, Long> {
    // Find all active groups (we don't want to show deleted ones)
    List<ClientGroup> findByIsActiveTrue();

    // Check if duplicate name exists
    boolean existsByGroupName(String groupName);
}