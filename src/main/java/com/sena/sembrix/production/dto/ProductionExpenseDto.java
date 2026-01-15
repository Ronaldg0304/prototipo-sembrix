package com.sena.sembrix.production.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductionExpenseDto {
    private Long id;
    private String description;
    private Double totalAmount;
    private Long inventoryId;
    private List<ProductionExpenseItemDto> items;
}

