package com.example.demo_sprinboot.mappers;

import com.example.demo_sprinboot.DTO.UserStoryDTO;
import com.example.demo_sprinboot.entities.UserStory;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserStoryMapper {
    UserStoryMapper INSTANCE = Mappers.getMapper(UserStoryMapper.class);
    UserStoryDTO toDto(UserStory userStory);
    UserStory toEntity(UserStoryDTO userStoryDTO);
}
