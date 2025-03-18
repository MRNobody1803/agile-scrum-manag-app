package com.example.demo_sprinboot.mappers;

import com.example.demo_sprinboot.DTO.UserStoryDTO;
import com.example.demo_sprinboot.entities.Priority;
import com.example.demo_sprinboot.entities.UserStory;
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
