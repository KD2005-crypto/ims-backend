package com.codeb.ims.service;

import com.codeb.ims.dto.ChainRequest;
import com.codeb.ims.entity.Chain;
import com.codeb.ims.entity.ClientGroup;
import com.codeb.ims.repository.ChainRepository;
import com.codeb.ims.repository.ClientGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ChainService {

    @Autowired
    private ChainRepository chainRepository;

    @Autowired
    private ClientGroupRepository groupRepository;

    public List<Chain> getAllChains() {
        return chainRepository.findByIsActiveTrue();
    }

    public Chain addChain(ChainRequest request) {
        // 1. Find the Group first
        ClientGroup group = groupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new RuntimeException("Group not found!"));

        // 2. Create the Chain
        Chain chain = new Chain();
        chain.setChainName(request.getChainName());
        chain.setGstNumber(request.getGstNumber());
        chain.setClientGroup(group); // Link them!

        return chainRepository.save(chain);
    }

    public void deleteChain(Long id) {
        Chain chain = chainRepository.findById(id).orElseThrow();
        chain.setActive(false);
        chainRepository.save(chain);
    }
}