package com.sena.sembrix.market.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketDataDto {
    private Long id;
    private String sourceApiName;
    private String externalProductId;
    private Double externalPrice;
    private LocalDateTime captureDate;
}

