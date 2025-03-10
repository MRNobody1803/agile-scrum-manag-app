package com.example.demo_sprinboot.services;


import com.example.demo_sprinboot.entities.ProductBacklog;
import com.example.demo_sprinboot.repository.ProductBacklogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductBacklogServiceImpl implements ProductBacklogService {
    private final ProductBacklogRepository productBacklogRepository;

    @Autowired
    public ProductBacklogServiceImpl(ProductBacklogRepository productBacklogRepository) {
        this.productBacklogRepository = productBacklogRepository;
    }

    @Override
    public List<ProductBacklog> getAllProductBacklogs() {
        return productBacklogRepository.findAll();
    }

    @Override
    public ProductBacklog getProductBacklogById(long id) {
        return productBacklogRepository.getReferenceById(id);
    }

    @Override
    public ProductBacklog createProductBacklog(ProductBacklog productBacklog) {
        return productBacklogRepository.save(productBacklog);
    }

    @Override
    public ProductBacklog updateProductBacklog(long id, ProductBacklog productBacklog) {
        Optional<ProductBacklog> existingProductBacklog = productBacklogRepository.findById(id);
        if (existingProductBacklog.isPresent()) {
            ProductBacklog existingProductBacklogEntity = existingProductBacklog.get();
            existingProductBacklogEntity.setName(productBacklog.getName());
            return productBacklogRepository.save(existingProductBacklogEntity);
        } else {
            throw new RuntimeException("Product backlog with id: " + id + " not found");
        }
    }

    @Override
    public void deleteProductBacklogById(long id) {
        Optional<ProductBacklog> existingProductBacklog = productBacklogRepository.findById(id);
        if (existingProductBacklog.isPresent()) {
            productBacklogRepository.deleteById(id);
        } else {
            throw new RuntimeException("Product backlog with id: " + id + " not found");
        }
    }
}