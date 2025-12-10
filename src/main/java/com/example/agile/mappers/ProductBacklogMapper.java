package com.example.agile.mappers;

import com.example.agile.dto.ProductBacklogDTO;
import com.example.agile.entities.ProductBacklog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProductBacklogMapper {
    ProductBacklogMapper INSTANCE = Mappers.getMapper(ProductBacklogMapper.class);
    @Mapping(source = "id", target = "id")
        // Remove or correct the problematic mappings
    ProductBacklogDTO toDTO(ProductBacklog productBacklog);

    // For the reverse mapping:
    ProductBacklog toEntity(ProductBacklogDTO productBacklogDTO);
    List<ProductBacklogDTO> toDtoList(List<ProductBacklog> productBacklogs);
}
