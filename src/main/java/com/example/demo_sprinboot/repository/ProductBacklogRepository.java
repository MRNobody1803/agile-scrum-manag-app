package com.example.demo_sprinboot.repository;

import com.example.demo_sprinboot.entities.ProductBacklog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductBacklogRepository extends JpaRepository<ProductBacklog, Long> {

}