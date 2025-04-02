package com.example.demo_sprinboot.repository;

import com.example.demo_sprinboot.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
