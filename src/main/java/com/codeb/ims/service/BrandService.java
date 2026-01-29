package com.codeb.ims.service;

import com.codeb.ims.dto.BrandRequest;
import com.codeb.ims.entity.Brand;
import com.codeb.ims.entity.Chain;
import com.codeb.ims.repository.BrandRepository;
import com.codeb.ims.repository.ChainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BrandService {

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private ChainRepository chainRepository;

    public List<Brand> getAllBrands() {
        return brandRepository.findByIsActiveTrue();
    }

    public Brand addBrand(BrandRequest request) {
        // 1. Find the Chain first
        Chain chain = chainRepository.findById(request.getChainId())
                .orElseThrow(() -> new RuntimeException("Chain not found!"));

        // 2. Create the Brand
        Brand brand = new Brand();
        brand.setBrandName(request.getBrandName());
        brand.setChain(chain);

        return brandRepository.save(brand);
    }

    public void deleteBrand(Long id) {
        Brand brand = brandRepository.findById(id).orElseThrow();
        brand.setActive(false);
        brandRepository.save(brand);
    }
}