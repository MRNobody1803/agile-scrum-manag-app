package com.example.demo_sprinboot.mappers;

import com.example.demo_sprinboot.DTO.SprintDTO;
import com.example.demo_sprinboot.entities.Sprint;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SprintMapper {
    SprintMapper INSTANCE = Mappers.getMapper(SprintMapper.class);
    SprintDTO toDto(Sprint sprint);
    Sprint toEntity(SprintDTO sprintDTO);
}
