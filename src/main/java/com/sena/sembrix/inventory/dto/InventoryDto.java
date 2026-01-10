package com.sena.sembrix.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryDto {
    private Long id;
    private Double currentStock;
    private Double unitPrice;
    private Double alertThreshold;
    private LocalDateTime lastUpdated;
    private Long productId;
    private Long profileProducerId;
}

