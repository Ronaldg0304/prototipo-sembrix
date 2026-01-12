package com.sena.sembrix.common.pricing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PriceAnalysisResponseDTO {
    private double unitCost;          // Lo que le cuesta producir 1 unidad
    private double marketAverage;     // Promedio en la región
    private double marginPercentage;  // Porcentaje de ganancia actual
    private String recommendation;    // Mensaje de la "IA"
    private String statusColor;       // SUCCESS, WARNING, DANGER
}