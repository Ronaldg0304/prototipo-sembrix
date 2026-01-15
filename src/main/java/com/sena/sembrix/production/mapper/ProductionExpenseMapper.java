package com.sena.sembrix.production.mapper;

import com.sena.sembrix.production.ProductionExpense;
import com.sena.sembrix.production.dto.ProductionExpenseDto;
import org.springframework.stereotype.Component;

@Component
public class ProductionExpenseMapper {

    public ProductionExpense toEntity(ProductionExpenseDto dto) {
        if (dto == null) return null;
        ProductionExpense e = new ProductionExpense();
        e.setId(dto.getId());
        e.setDescription(dto.getDescription());
        e.setTotalAmount(dto.getTotalAmount());
        return e;
    }

    public ProductionExpenseDto toDto(ProductionExpense entity) {
        if (entity == null) return null;
        ProductionExpenseDto dto = new ProductionExpenseDto();
        dto.setId(entity.getId());
        dto.setDescription(entity.getDescription());
        dto.setTotalAmount(entity.getTotalAmount());
        return dto;
    }
}

