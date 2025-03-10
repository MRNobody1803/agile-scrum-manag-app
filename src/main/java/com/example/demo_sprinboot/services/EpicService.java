package com.example.demo_sprinboot.services;

import com.example.demo_sprinboot.DTO.EpicDTO;
import java.util.List;

public interface EpicService {
    EpicDTO createEpic(EpicDTO epicDTO);
    EpicDTO updateEpic(Long id, EpicDTO epicDTO);
    void deleteEpic(Long id);
    EpicDTO getEpicById(Long id);
    List<EpicDTO> getAllEpics();
}