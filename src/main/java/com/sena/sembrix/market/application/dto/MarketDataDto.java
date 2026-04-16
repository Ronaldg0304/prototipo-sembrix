package com.sena.sembrix.market.application.dto;

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
    private String region;
    private LocalDateTime captureDate;
}

