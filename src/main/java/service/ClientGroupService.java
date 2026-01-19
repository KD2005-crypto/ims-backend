package com.codeb.ims.service;

import com.codeb.ims.entity.ClientGroup;
import com.codeb.ims.repository.ClientGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ClientGroupService {

    @Autowired
    private ClientGroupRepository repository;

    // 1. Get All Groups
    public List<ClientGroup> getAllGroups() {
        return repository.findByIsActiveTrue();
    }

    // 2. Add New Group
    public ClientGroup addGroup(String name) {
        if (repository.existsByGroupName(name)) {
            throw new RuntimeException("Group name already exists!");
        }
        ClientGroup group = new ClientGroup();
        group.setGroupName(name);
        return repository.save(group);
    }

    // 3. Soft Delete Group (Just hide it, don't destroy data)
    public void deleteGroup(Long id) {
        ClientGroup group = repository.findById(id).orElseThrow();
        group.setActive(false);
        repository.save(group);
    }
}