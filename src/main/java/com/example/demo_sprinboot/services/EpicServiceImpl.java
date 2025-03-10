package com.example.demo_sprinboot.services;

import com.example.demo_sprinboot.DTO.EpicDTO;
import com.example.demo_sprinboot.entities.Epic;
import com.example.demo_sprinboot.entities.ProductBacklog;
import com.example.demo_sprinboot.repository.ProductBacklogRepository;
import com.example.demo_sprinboot.repository.EpicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EpicServiceImpl implements EpicService {

    @Autowired
    private EpicRepository epicRepository;

    @Autowired
    private ProductBacklogRepository productBacklogRepository;

    @Override
    public EpicDTO createEpic(EpicDTO epicDTO) {
        ProductBacklog productBacklog = productBacklogRepository.findById(epicDTO.getProductBacklogId())
                .orElseThrow(() -> new RuntimeException("ProductBacklog not found"));

        Epic epic = new Epic();
        epic.setName(epicDTO.getName());
        epic.setDescription(epicDTO.getDescription());
        epic.setProductBacklog(productBacklog);

        Epic savedEpic = epicRepository.save(epic);

        return new EpicDTO(savedEpic.getId(), savedEpic.getName(),
                savedEpic.getDescription(), savedEpic.getProductBacklog().getId(),
                null);
    }

    @Override
    public EpicDTO updateEpic(Long id, EpicDTO epicDTO) {

        Epic existingEpic = epicRepository.findById(id).orElseThrow(() -> new RuntimeException("Epic not found"));

        existingEpic.setName(epicDTO.getName());
        existingEpic.setDescription(epicDTO.getDescription());

        Epic updatedEpic = epicRepository.save(existingEpic);

        return new EpicDTO(
                updatedEpic.getId(), updatedEpic.getName(),
                updatedEpic.getDescription(), updatedEpic.getProductBacklog().getId(),
                null);
    }

    @Override
    public void deleteEpic(Long id) {
        Epic epic = epicRepository.findById(id).orElseThrow(() -> new RuntimeException("Epic not found"));
        epicRepository.delete(epic);
    }

    @Override
    public EpicDTO getEpicById(Long id) {

        Epic epic = epicRepository.findById(id).orElseThrow(() -> new RuntimeException("Epic not found"));
        return new EpicDTO(epic.getId(), epic.getName(),
                epic.getDescription(), epic.getProductBacklog().getId(),
                null);
    }

    @Override
    public List<EpicDTO> getAllEpics() {

        List<Epic> epics = epicRepository.findAll();
        return epics.stream()
                .map(epic -> new EpicDTO(epic.getId(), epic.getName(),
                        epic.getDescription(), epic.getProductBacklog().getId(),
                        null)).collect(Collectors.toList());
    }
}