package com.example.demo_sprinboot.repository;


import com.example.demo_sprinboot.entities.UserStory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStoryRepository extends JpaRepository<UserStory, Long> {

}
