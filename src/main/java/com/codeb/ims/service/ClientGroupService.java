package com.codeb.ims.service;

import com.codeb.ims.entity.ClientGroup;
import com.codeb.ims.repository.ClientGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ClientGroupService {

    @Autowired
    private ClientGroupRepository repository;

    // 1. Get All Groups
    public List<ClientGroup> getAllGroups() {
        return repository.findByIsActiveTrue();
    }

    // 2. Add New Group (FIXED: Handles "Ghost" Records)
    public ClientGroup addGroup(String name) {
        // Try to find the group (Active OR Inactive)
        Optional<ClientGroup> existingGroup = repository.findByGroupName(name);

        if (existingGroup.isPresent()) {
            ClientGroup group = existingGroup.get();

            // If it exists and is ACTIVE -> Duplicate Error
            if (Boolean.TRUE.equals(group.isActive())) {
                throw new RuntimeException("Group name already exists!");
            }
            // If it exists but is INACTIVE (Deleted) -> Reactivate it!
            else {
                group.setActive(true);
                return repository.save(group);
            }
        }

        // If it doesn't exist at all -> Create New
        ClientGroup group = new ClientGroup();
        group.setGroupName(name);
        group.setActive(true);
        return repository.save(group);
    }

    // 3. Soft Delete Group
    public void deleteGroup(Long id) {
        ClientGroup group = repository.findById(id).orElseThrow();
        group.setActive(false);
        repository.save(group);
    }
}