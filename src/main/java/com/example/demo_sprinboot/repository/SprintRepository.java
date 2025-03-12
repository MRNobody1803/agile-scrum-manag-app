package com.example.demo_sprinboot.repository;

import com.example.demo_sprinboot.entities.Sprint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SprintRepository extends JpaRepository<Sprint, Long> {
}
