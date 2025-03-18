package com.example.demo_sprinboot.mappers;

import com.example.demo_sprinboot.DTO.EpicDTO;
import com.example.demo_sprinboot.entities.Epic;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface EpicMapper {

    @Mapping(source = "productBacklog.id", target = "productBacklogId")
    EpicDTO toDTO(Epic epic);

    @Mapping(target = "productBacklog", ignore = true)
    @Mapping(target = "userStories", ignore = true)
    Epic toEntity(EpicDTO epicDTO);
}
