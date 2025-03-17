package com.example.demo_sprinboot.services;


import com.example.demo_sprinboot.DTO.ProductBacklogDTO;

import java.util.List;

public interface ProductBacklogService {
    List<ProductBacklogDTO> getAllProductBacklogs();
    ProductBacklogDTO getProductBacklogById(long id);
    ProductBacklogDTO createProductBacklog(ProductBacklogDTO productBacklog);
    ProductBacklogDTO updateProductBacklog(long id, ProductBacklogDTO productBacklog);
    boolean deleteProductBacklogById(long id);
}