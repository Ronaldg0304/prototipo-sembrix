package com.sena.sembrix.inventory.mapper;

import com.sena.sembrix.inventory.Product;
import com.sena.sembrix.inventory.dto.ProductDto;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toEntity(ProductDto dto) {
        if (dto == null) return null;
        Product e = new Product();
        e.setId(dto.getId());
        e.setName(dto.getName());
        e.setDescription(dto.getDescription());
        e.setCategory(dto.getCategory());
        e.setUnitOfMeasure(dto.getUnitOfMeasure());
        return e;
    }

    public ProductDto toDto(Product entity) {
        if (entity == null) return null;
        ProductDto dto = new ProductDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setCategory(entity.getCategory());
        dto.setUnitOfMeasure(entity.getUnitOfMeasure());
        return dto;
    }
}

