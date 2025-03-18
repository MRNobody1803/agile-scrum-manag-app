package com.example.demo_sprinboot.services;

import com.example.demo_sprinboot.DTO.AcceptanceCriteriaDTO;
import com.example.demo_sprinboot.entities.AcceptanceCriteria;
import com.example.demo_sprinboot.mappers.AcceptanceCriteriaMapper;
import com.example.demo_sprinboot.repository.AcceptanceCriteriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AcceptanceCriteriaServiceImpl implements AcceptanceCriteriaService {

    private final AcceptanceCriteriaRepository acceptanceCriteriaRepository;
    private final AcceptanceCriteriaMapper acceptanceCriteriaMapper;

    @Autowired
    public AcceptanceCriteriaServiceImpl(AcceptanceCriteriaRepository acceptanceCriteriaRepository, AcceptanceCriteriaMapper acceptanceCriteriaMapper) {
        this.acceptanceCriteriaRepository = acceptanceCriteriaRepository;
        this.acceptanceCriteriaMapper = acceptanceCriteriaMapper;
    }

    @Override
    public List<AcceptanceCriteriaDTO> getAllAcceptanceCriterias() {
        List<AcceptanceCriteria> acceptanceCriterias = acceptanceCriteriaRepository.findAll();
        return acceptanceCriterias.stream()
                .map(acceptanceCriteriaMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<AcceptanceCriteriaDTO> getAcceptanceCriteriaById(Long id) {
        Optional<AcceptanceCriteria> acceptanceCriteria = acceptanceCriteriaRepository.findById(id);
        return acceptanceCriteria.map(acceptanceCriteriaMapper::toDto);
    }

    @Override
    public AcceptanceCriteriaDTO createAcceptanceCriteria(AcceptanceCriteria acceptanceCriteria) {
        AcceptanceCriteria savedAcceptanceCriteria = acceptanceCriteriaRepository.save(acceptanceCriteria);
        return acceptanceCriteriaMapper.toDto(savedAcceptanceCriteria);
    }

    @Override
    public AcceptanceCriteriaDTO updateAcceptanceCriteria(Long id, AcceptanceCriteria acceptanceCriteria) {
        if (!acceptanceCriteriaRepository.existsById(id)) {
            throw new RuntimeException("AcceptanceCriteria not found with id " + id);
        }
        acceptanceCriteria.setId(id);
        AcceptanceCriteria updatedAcceptanceCriteria = acceptanceCriteriaRepository.save(acceptanceCriteria);
        return acceptanceCriteriaMapper.toDto(updatedAcceptanceCriteria);
    }

    @Override
    public boolean deleteAcceptanceCriteria(Long id) {
        if (!acceptanceCriteriaRepository.existsById(id)) {
            throw new RuntimeException("AcceptanceCriteria not found with id " + id);
        }
        acceptanceCriteriaRepository.deleteById(id);
        return false;
    }
}
