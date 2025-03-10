package com.example.demo_sprinboot.repository;

import com.example.demo_sprinboot.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}