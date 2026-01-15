package com.sena.sembrix.market.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketPriceDto {
    private Long id;
    private String region;
    private Double averageMarketPrice;
    private Double maxPrice;
    private Double minPrice;
    private String trend;
    private Long productId;
    private String productName;
    private String productCategory;
}

