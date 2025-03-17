package com.example.demo_sprinboot.services;

import com.example.demo_sprinboot.DTO.ProductBacklogDTO;
import com.example.demo_sprinboot.entities.ProductBacklog;
import com.example.demo_sprinboot.exceptions.ProductBacklogNotFoundException;
import com.example.demo_sprinboot.mappers.ProductBacklogMapper;
import com.example.demo_sprinboot.repository.ProductBacklogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductBacklogServiceImpl implements ProductBacklogService {

    private final ProductBacklogRepository productBacklogRepository;
    private final ProductBacklogMapper productBacklogMapper;

    @Autowired
    public ProductBacklogServiceImpl(ProductBacklogRepository productBacklogRepository, ProductBacklogMapper productBacklogMapper) {
        this.productBacklogRepository = productBacklogRepository;
        this.productBacklogMapper = productBacklogMapper;
    }

    @Override
    @Cacheable(value = "productBacklogs")
    public List<ProductBacklogDTO> getAllProductBacklogs() {
        List<ProductBacklog> productBacklogs = productBacklogRepository.findAll();
        return productBacklogMapper.toDtoList(productBacklogs);
    }

    @Override
    @Cacheable(value = "productBacklogs", key = "#id")
    public ProductBacklogDTO getProductBacklogById(long id) {
        ProductBacklog productBacklog = productBacklogRepository.findById(id)
                .orElseThrow(() -> new ProductBacklogNotFoundException("Product backlog with id: " + id + " not found"));
        return productBacklogMapper.toDto(productBacklog);
    }

    @Override
    @Transactional
    @CacheEvict(value = "productBacklogs", allEntries = true) // Clear cache after creation
    public ProductBacklogDTO createProductBacklog(ProductBacklogDTO productBacklogDTO) {
        ProductBacklog productBacklog = productBacklogMapper.toEntity(productBacklogDTO);
        ProductBacklog savedProductBacklog = productBacklogRepository.save(productBacklog);
        return productBacklogMapper.toDto(savedProductBacklog);
    }

    @Override
    @Transactional
    @CacheEvict(value = "productBacklogs", allEntries = true) // Clear cache after update
    public ProductBacklogDTO updateProductBacklog(long id, ProductBacklogDTO productBacklogDTO) {
        ProductBacklog productBacklog = productBacklogRepository.findById(id)
                .orElseThrow(() -> new ProductBacklogNotFoundException("Product backlog with id: " + id + " not found"));
        productBacklog.setName(productBacklogDTO.getName());
        ProductBacklog updatedProductBacklog = productBacklogRepository.save(productBacklog);
        return productBacklogMapper.toDto(updatedProductBacklog);
    }

    @Override
    @Transactional
    @CacheEvict(value = "productBacklogs", key = "#id")
    public boolean deleteProductBacklogById(long id) {
        ProductBacklog productBacklog = productBacklogRepository.findById(id)
                .orElseThrow(() -> new ProductBacklogNotFoundException("Product backlog with id: " + id + " not found"));
        productBacklogRepository.deleteById(id);
        return true;
    }

}
