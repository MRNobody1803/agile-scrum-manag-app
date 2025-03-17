package com.example.demo_sprinboot.services;

import com.example.demo_sprinboot.DTO.SprintDTO;
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
