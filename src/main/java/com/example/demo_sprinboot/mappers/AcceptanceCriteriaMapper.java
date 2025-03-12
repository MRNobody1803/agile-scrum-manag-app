package com.example.demo_sprinboot.mappers;

import com.example.demo_sprinboot.DTO.AcceptanceCriteriaDTO;
import com.example.demo_sprinboot.entities.AcceptanceCriteria;
import org.mapstruct.Mapper;

@Mapper
public interface AcceptanceCriteriaMapper {
    AcceptanceCriteriaDTO toDto(AcceptanceCriteria acceptanceCriteria);
    AcceptanceCriteria toEntity(AcceptanceCriteriaDTO acceptanceCriteriaDTO);
}
