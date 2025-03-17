package com.example.demo_sprinboot.services;

import com.example.demo_sprinboot.DTO.SprintDTO;
import com.example.demo_sprinboot.entities.Sprint;
import com.example.demo_sprinboot.entities.Status;
import com.example.demo_sprinboot.mappers.SprintMapper;
import com.example.demo_sprinboot.repository.SprintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SprintServiceImpl implements SprintService {

    private final SprintRepository sprintRepository;
    private final SprintMapper sprintMapper;

    @Autowired
    public SprintServiceImpl(SprintRepository sprintRepository, SprintMapper sprintMapper) {
        this.sprintRepository = sprintRepository;
        this.sprintMapper = sprintMapper;
    }

    @Override
    @Cacheable(value = "sprints")
    public List<SprintDTO> getAllSprints() {
        List<Sprint> sprints = sprintRepository.findAll();
        return sprints.stream()
                .map(sprintMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "sprints", key = "#id")
    public Optional<SprintDTO> getSprintById(Long id) {
        return sprintRepository.findById(id)
                .map(sprintMapper::toDto);
    }

    @Override
    @CacheEvict(value = "sprints", allEntries = true)
    public SprintDTO createSprint(SprintDTO sprintDTO) {
        Sprint sprint = sprintMapper.toEntity(sprintDTO);
        sprint = sprintRepository.save(sprint);
        return sprintMapper.toDto(sprint);
    }

    @Override
    @CacheEvict(value = "sprints", key = "#id")
    public SprintDTO updateSprint(Long id, SprintDTO sprintDTO) {
        return sprintRepository.findById(id).map(existingSprint -> {
            existingSprint.setName(sprintDTO.getName());
            existingSprint.setStartDate(sprintDTO.getStartDate());
            existingSprint.setEndDate(sprintDTO.getEndDate());
            existingSprint.setStatus(sprintDTO.getStatus());
            Sprint updatedSprint = sprintRepository.save(existingSprint);
            return sprintMapper.toDto(updatedSprint);
        }).orElseThrow(() -> new RuntimeException("Sprint with id " + id + " not found"));
    }

    @Override
    @CacheEvict(value = "sprints", key = "#id")
    public void deleteSprint(Long id) {
        if (!sprintRepository.existsById(id)) {
            throw new RuntimeException("Sprint with id " + id + " not found");
        }
        sprintRepository.deleteById(id);
    }

    @Override
    @CacheEvict(value = "sprints", key = "#id")
    public void startSprint(Long id) {
        Sprint sprint = sprintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sprint with id " + id + " not found"));
        sprint.setStatus(Status.IN_PROGRESS);
        sprintRepository.save(sprint);
    }

    @Override
    @CacheEvict(value = "sprints", key = "#id")
    public void completeSprint(Long id) {
        Sprint sprint = sprintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sprint with id " + id + " not found"));
        sprint.setStatus(Status.DONE);
        sprintRepository.save(sprint);
    }
}
