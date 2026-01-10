package com.sena.sembrix.production.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductionExpenseItemDto {
    private Long id;
    private String description;
    private Double amount;
    private LocalDateTime expenseDate;
    private String category;
    private Long inventoryId;
    private Long productionExpenseId;
}

