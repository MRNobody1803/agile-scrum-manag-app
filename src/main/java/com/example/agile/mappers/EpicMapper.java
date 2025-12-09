package com.example.agile.mappers;

import com.example.agile.DTO.EpicDTO;
import com.example.agile.entities.Epic;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EpicMapper {

    @Mapping(source = "productBacklog.id", target = "productBacklogId")
    EpicDTO toDTO(Epic epic);

    @Mapping(target = "productBacklog", ignore = true)
    @Mapping(target = "userStories", ignore = true)
    Epic toEntity(EpicDTO epicDTO);
}
