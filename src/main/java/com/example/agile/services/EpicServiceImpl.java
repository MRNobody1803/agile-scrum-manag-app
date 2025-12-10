package com.example.agile.services;

import com.example.agile.dto.EpicDTO;
import com.example.agile.entities.Epic;
import com.example.agile.entities.ProductBacklog;
import com.example.agile.exceptions.ResourceNotFoundException;
import com.example.agile.mappers.EpicMapper;
import com.example.agile.repository.ProductBacklogRepository;
import com.example.agile.repository.EpicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EpicServiceImpl implements EpicService {

    private static final String NOT_FOUND = "Epic not found";
    private final EpicRepository epicRepository;
    private final ProductBacklogRepository productBacklogRepository;
    private final EpicMapper epicMapper ;

    @Autowired
    public EpicServiceImpl(EpicRepository epicRepository, ProductBacklogRepository productBacklogRepository , EpicMapper epicMapper) {
        this.epicRepository = epicRepository;
        this.productBacklogRepository = productBacklogRepository;
        this.epicMapper = epicMapper;
    }

    @Override
    @CacheEvict(value = "epics", allEntries = true)
    public EpicDTO createEpic(EpicDTO epicDTO) {
        ProductBacklog productBacklog = productBacklogRepository.findById(epicDTO.getProductBacklogId())
                .orElseThrow(() -> new ResourceNotFoundException("ProductBacklog not found"));

        Epic epic = epicMapper.toEntity(epicDTO);
        epic.setProductBacklog(productBacklog);

        Epic savedEpic = epicRepository.save(epic);
        return epicMapper.toDTO(savedEpic);
    }

    @Override
    @CacheEvict(value = "epics", key = "#id")
    public EpicDTO updateEpic(Long id, EpicDTO epicDTO) {
        Epic existingEpic = epicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND));

        existingEpic.setName(epicDTO.getName());
        existingEpic.setDescription(epicDTO.getDescription());

        if (epicDTO.getProductBacklogId() != null) {
            ProductBacklog productBacklog = productBacklogRepository.findById(epicDTO.getProductBacklogId())
                    .orElseThrow(() -> new ResourceNotFoundException("ProductBacklog not found"));
            existingEpic.setProductBacklog(productBacklog);
        }

        Epic updatedEpic = epicRepository.save(existingEpic);
        return epicMapper.toDTO(updatedEpic);
    }

    @Override
    @CacheEvict(value = "epics", allEntries = true)
    public boolean deleteEpic(Long id) {
        Epic epic = epicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND));

        if (!epic.getUserStories().isEmpty()) {
            throw new IllegalStateException("Cannot delete Epic with existing User Stories");
        }

        epicRepository.delete(epic);
        return false;
    }

    @Override
    @Cacheable("epics")
    public EpicDTO getEpicById(Long id) {
        Epic epic = epicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND));
        return epicMapper.toDTO(epic);
    }

    @Override
    @Cacheable("epics")
    public List<EpicDTO> getAllEpics() {
        return epicRepository.findAll().stream()
                .map(epicMapper::toDTO)
                .collect(Collectors.toList());
    }
}
