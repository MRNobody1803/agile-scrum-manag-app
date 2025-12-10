package com.example.agile.services;

import com.example.agile.dto.SprintBacklogDTO;

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
