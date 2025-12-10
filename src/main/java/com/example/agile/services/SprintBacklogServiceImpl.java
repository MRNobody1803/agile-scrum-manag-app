package com.example.agile.services;

import com.example.agile.dto.SprintBacklogDTO;
import com.example.agile.entities.SprintBacklog;
import com.example.agile.exceptions.UserNotFoundException;
import com.example.agile.mappers.SprintBacklogMapper;
import com.example.agile.repository.SprintBacklogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SprintBacklogServiceImpl implements SprintBacklogService {

    private final SprintBacklogRepository sprintBacklogRepository;
    private final SprintBacklogMapper sprintBacklogMapper;

    @Autowired
    public SprintBacklogServiceImpl(SprintBacklogRepository sprintBacklogRepository, SprintBacklogMapper sprintBacklogMapper) {
        this.sprintBacklogRepository = sprintBacklogRepository;
        this.sprintBacklogMapper = sprintBacklogMapper;
    }

    @Override
    @Cacheable(value = "sprintBacklogs")
    public List<SprintBacklogDTO> getAllSprintBacklogs() {
        List<SprintBacklog> sprintBacklogs = sprintBacklogRepository.findAll();
        return sprintBacklogs.stream()
                .map(sprintBacklogMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "sprintBacklogs", key = "#id")
    public Optional<SprintBacklogDTO> getSprintBacklogById(Long id) {
        return sprintBacklogRepository.findById(id)
                .map(sprintBacklogMapper::toDto);
    }

    @Override
    @CacheEvict(value = "sprintBacklogs", allEntries = true)
    public SprintBacklogDTO createSprintBacklog(SprintBacklogDTO sprintBacklogDTO) {
        SprintBacklog sprintBacklog = sprintBacklogMapper.toEntity(sprintBacklogDTO);
        SprintBacklog savedSprintBacklog = sprintBacklogRepository.save(sprintBacklog);
        return sprintBacklogMapper.toDto(savedSprintBacklog);
    }

    @Override
    @CacheEvict(value = "sprintBacklogs", key = "#id")
    public SprintBacklogDTO updateSprintBacklog(Long id, SprintBacklogDTO sprintBacklogDTO) {
        return sprintBacklogRepository.findById(id).map(existingSprintBacklog -> {
            existingSprintBacklog.setName(sprintBacklogDTO.getName());
            existingSprintBacklog.setUserStories(sprintBacklogMapper.toEntity(sprintBacklogDTO).getUserStories());

            SprintBacklog updatedSprintBacklog = sprintBacklogRepository.save(existingSprintBacklog);
            return sprintBacklogMapper.toDto(updatedSprintBacklog);
        }).orElseThrow(() -> new RuntimeException("SprintBacklog avec l'ID " + id + " introuvable"));
    }

    @Override
    @CacheEvict(value = "sprintBacklogs", key = "#id")
    public void deleteSprintBacklog(Long id) {
        if (!sprintBacklogRepository.existsById(id)) {
            throw new RuntimeException("SprintBacklog avec l'ID " + id + " introuvable");
        }
        sprintBacklogRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateSprintBacklogName(Long id, String newName) {
        if (!sprintBacklogRepository.existsById(id)) {
            throw new UserNotFoundException("SprintBacklog with id " + id + " not found");
        }
        sprintBacklogRepository.updateSprintBacklogName(id, newName);
    }

    @Override
    public List<SprintBacklogDTO> searchSprintBacklogByName(String keyword) {
        List<SprintBacklog> results = sprintBacklogRepository.searchByNom(keyword);
        return results.stream()
                .map(sprintBacklogMapper::toDto)
                .collect(Collectors.toList());
    }


}
