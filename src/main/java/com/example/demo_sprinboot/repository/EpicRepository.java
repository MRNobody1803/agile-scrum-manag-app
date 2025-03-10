package com.example.demo_sprinboot.repository;

import com.example.demo_sprinboot.entities.Epic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EpicRepository extends JpaRepository<Epic, Long> {

}
