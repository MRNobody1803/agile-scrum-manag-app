package com.example.demo_sprinboot.services;

import com.example.demo_sprinboot.DTO.SprintBacklogDTO;

import java.util.List;
import java.util.Optional;

public interface SprintBacklogService {
    List<SprintBacklogDTO> getAllSprintBacklogs();
    Optional<SprintBacklogDTO> getSprintBacklogById(Long id);
    SprintBacklogDTO createSprintBacklog(SprintBacklogDTO sprintBacklogDTO);
    SprintBacklogDTO updateSprintBacklog(Long id, SprintBacklogDTO sprintBacklogDTO);
    void deleteSprintBacklog(Long id);
    public List<SprintBacklogDTO> searchSprintBacklogByName(String keyword);
    public void updateSprintBacklogName(Long id, String newName);
}
