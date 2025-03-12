package com.example.demo_sprinboot.mappers;

import com.example.demo_sprinboot.DTO.SprintDTO;
import com.example.demo_sprinboot.entities.Sprint;
import org.mapstruct.Mapper;

@Mapper
public interface SprintMapper {
    SprintDTO toDto(Sprint sprint);
    Sprint toEntity(SprintDTO sprintDTO);
}
