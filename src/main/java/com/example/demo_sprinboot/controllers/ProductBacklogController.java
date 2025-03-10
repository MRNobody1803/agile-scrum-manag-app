package com.example.demo_sprinboot.controllers;

import com.example.demo_sprinboot.entities.ProductBacklog;
import com.example.demo_sprinboot.services.ProductBacklogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/backlogs")
public class ProductBacklogController {

    @Autowired
    private ProductBacklogService productBacklogService;

    @GetMapping
    public ResponseEntity<List<ProductBacklog>> getAllProductBacklogs() {
        List<ProductBacklog> backlogs = productBacklogService.getAllProductBacklogs();
        return ResponseEntity.ok().body(backlogs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductBacklog> getProductBacklogById(@PathVariable long id) {
        ProductBacklog backlog = productBacklogService.getProductBacklogById(id);
        return ResponseEntity.ok().body(backlog);
    }


    @PostMapping
    public ResponseEntity<ProductBacklog> createProductBacklog(@RequestBody ProductBacklog productBacklog) {
        ProductBacklog createdBacklog = productBacklogService.createProductBacklog(productBacklog);
        return ResponseEntity.status(201).body(createdBacklog);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductBacklog> updateProductBacklog(@PathVariable long id, @RequestBody ProductBacklog productBacklog) {
        productBacklog.setId(id);
        ProductBacklog backlog = productBacklogService.updateProductBacklog(id,productBacklog);
        return ResponseEntity.ok().body(backlog);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProductBacklog> deleteProductBacklog(@PathVariable long id) {
        productBacklogService.deleteProductBacklogById(id);
        return ResponseEntity.noContent().build();
    }

}

