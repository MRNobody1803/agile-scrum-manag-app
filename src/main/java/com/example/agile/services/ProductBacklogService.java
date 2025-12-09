package com.example.agile.services;


import com.example.agile.DTO.ProductBacklogDTO;

import java.util.List;

public interface ProductBacklogService {
    List<ProductBacklogDTO> getAllProductBacklogs();
    ProductBacklogDTO getProductBacklogById(long id);
    ProductBacklogDTO createProductBacklog(ProductBacklogDTO productBacklog);
    ProductBacklogDTO updateProductBacklog(long id, ProductBacklogDTO productBacklog);
    boolean deleteProductBacklogById(long id);
}