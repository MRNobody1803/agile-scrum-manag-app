package com.example.agile.services;

import com.example.agile.DTO.SprintDTO;
import java.util.List;
import java.util.Optional;

public interface SprintService {
    List<SprintDTO> getAllSprints();
    Optional<SprintDTO> getSprintById(Long id);
    SprintDTO createSprint(SprintDTO sprintDTO);
    SprintDTO updateSprint(Long id, SprintDTO sprintDTO);
    void deleteSprint(Long id);
    void startSprint(Long id);
    void completeSprint(Long id);
}
