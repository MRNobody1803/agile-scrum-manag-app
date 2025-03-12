package com.example.demo_sprinboot.mappers;

import com.example.demo_sprinboot.DTO.EpicDTO;
import com.example.demo_sprinboot.entities.Epic;
import org.mapstruct.Mapper;

@Mapper
public interface EpicMapper {
    EpicDTO toDto(Epic epic);
    Epic toEntity(EpicDTO epicDTO);
}
