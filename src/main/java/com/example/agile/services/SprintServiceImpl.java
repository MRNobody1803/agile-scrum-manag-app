package com.example.agile.services;

import com.example.agile.dto.SprintDTO;
import com.example.agile.entities.Sprint;
import com.example.agile.entities.Status;
import com.example.agile.exceptions.ResourceNotFoundException;
import com.example.agile.mappers.SprintMapper;
import com.example.agile.repository.SprintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SprintServiceImpl implements SprintService {

    private static final String ID = "Sprint with id: ";
    private static final String NOT_FOUND = "not found";
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
        }).orElseThrow(() -> new RuntimeException(ID + id + NOT_FOUND));
    }

    @Override
    @CacheEvict(value = "sprints", key = "#id")
    public void deleteSprint(Long id) {
        if (!sprintRepository.existsById(id)) {
            throw new ResourceNotFoundException(ID + id + NOT_FOUND);
        }
        sprintRepository.deleteById(id);
    }

    @Override
    @CacheEvict(value = "sprints", key = "#id")
    public void startSprint(Long id) {
        Sprint sprint = sprintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(ID + id + NOT_FOUND));
        sprint.setStatus(Status.IN_PROGRESS);
        sprintRepository.save(sprint);
    }

    @Override
    @CacheEvict(value = "sprints", key = "#id")
    public void completeSprint(Long id) {
        Sprint sprint = sprintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(ID + id + NOT_FOUND));
        sprint.setStatus(Status.DONE);
        sprintRepository.save(sprint);
    }
}
