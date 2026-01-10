package com.sena.sembrix.identity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileProducerDto {
    private Long id;
    private String farmName;
    private String region;
    private String municipality;
    private Double farmSize;
    private String associationName;
    private Long userId;
}

