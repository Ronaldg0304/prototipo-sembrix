package com.sena.sembrix.sales.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleDto {
    private Long id;
    private Double salePrice;
    private Double totalCost;
    private Double profit;
    private LocalDateTime date;
    private Long invoiceId;
    private Long profileProducerId;
}

