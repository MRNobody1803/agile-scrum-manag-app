package com.example.agile.mappers;

import com.example.agile.DTO.UserStoryDTO;
import com.example.agile.entities.Priority;
import com.example.agile.entities.UserStory;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserStoryMapper {
    UserStoryMapper INSTANCE = Mappers.getMapper(UserStoryMapper.class);
    UserStoryDTO toDto(UserStory userStory);
    UserStory toEntity(UserStoryDTO userStoryDTO);
    default Priority mapToPriority(int priority) {
        return Priority.values()[priority];  // Assuming Priority enum has values indexed from 0
    }

    default int mapToInt(Priority priority) {
        return priority.ordinal();
    }
}
