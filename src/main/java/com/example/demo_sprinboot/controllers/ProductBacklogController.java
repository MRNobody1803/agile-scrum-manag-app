package com.example.demo_sprinboot.controllers;

import com.example.demo_sprinboot.DTO.ProductBacklogDTO;
import com.example.demo_sprinboot.services.ProductBacklogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/backlogs")
public class ProductBacklogController {

    private final ProductBacklogService productBacklogService;

    @Autowired
    public ProductBacklogController(ProductBacklogService productBacklogService) {
        this.productBacklogService = productBacklogService;
    }

    @GetMapping
    public ResponseEntity<List<ProductBacklogDTO>> getAllProductBacklogs() {
        List<ProductBacklogDTO> backlogs = productBacklogService.getAllProductBacklogs();
        return ResponseEntity.ok(backlogs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductBacklogDTO> getProductBacklogById(@PathVariable long id) {
        ProductBacklogDTO backlog = productBacklogService.getProductBacklogById(id);
        if (backlog != null) {
            return ResponseEntity.ok(backlog);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<ProductBacklogDTO> createProductBacklog(@RequestBody ProductBacklogDTO productBacklogDTO) {
        ProductBacklogDTO createdBacklog = productBacklogService.createProductBacklog(productBacklogDTO);
        return ResponseEntity.status(201).body(createdBacklog);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductBacklogDTO> updateProductBacklog(@PathVariable long id, @RequestBody ProductBacklogDTO productBacklogDTO) {
        ProductBacklogDTO updatedBacklog = productBacklogService.updateProductBacklog(id, productBacklogDTO);
        if (updatedBacklog != null) {
            return ResponseEntity.ok(updatedBacklog);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductBacklog(@PathVariable long id) {
        boolean isDeleted = productBacklogService.deleteProductBacklogById(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
