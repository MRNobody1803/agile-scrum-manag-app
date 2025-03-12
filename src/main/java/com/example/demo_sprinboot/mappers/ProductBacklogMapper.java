package com.example.demo_sprinboot.mappers;

import com.example.demo_sprinboot.DTO.ProductBacklogDTO;
import com.example.demo_sprinboot.entities.ProductBacklog;
import org.mapstruct.Mapper;

@Mapper
public interface ProductBacklogMapper {
    ProductBacklogDTO toDto(ProductBacklog productBacklog);
    ProductBacklog toEntity(ProductBacklogDTO productBacklogDTO);
}
