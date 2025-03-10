package com.example.demo_sprinboot.services;


import com.example.demo_sprinboot.entities.ProductBacklog;

import java.util.List;

public interface ProductBacklogService {
    List<ProductBacklog> getAllProductBacklogs();
    ProductBacklog getProductBacklogById(long id);
    ProductBacklog createProductBacklog(ProductBacklog productBacklog);
    ProductBacklog updateProductBacklog(long id, ProductBacklog productBacklog);
    void deleteProductBacklogById(long id);
}