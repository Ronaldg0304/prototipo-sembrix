package com.sena.sembrix.common.pricing.service;

import com.sena.sembrix.common.pricing.dto.PriceAnalysisResponseDTO;
import com.sena.sembrix.inventory.Inventory;
import com.sena.sembrix.market.dto.MarketPriceDto;
import com.sena.sembrix.market.service.MarketPriceService;
import com.sena.sembrix.production.service.ProductionExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PricingEngineService {

    private final ProductionExpenseService expenseService;
    private final MarketPriceService marketPriceService;

    public PriceAnalysisResponseDTO analyzeInventoryPrice(Inventory inventory) {
        // 1. Calcular Costo de Producción
        double unitCost = expenseService.calculateUnitCost(inventory.getId(), inventory.getCurrentStock());

        // 2. Obtener Referencia de Mercado (si existe)
        double marketAvg = marketPriceService.getRegionalPrice(
                inventory.getProduct().getId(),
                inventory.getProfileProducer().getRegion()
        ).map(MarketPriceDto::getAverageMarketPrice).orElse(0.0);

        // 3. Calcular Margen Actual
        double producerPrice = inventory.getUnitPrice();
        double margin = (producerPrice > 0) ? ((producerPrice - unitCost) / producerPrice) * 100 : 0;

        // 4. Generar Recomendación basada en reglas
        String recommendation;
        String color;

        if (producerPrice < unitCost) {
            recommendation = "Alerta Crítica: Tu precio está por debajo del costo de producción. Estás perdiendo dinero.";
            color = "DANGER";
        } else if (marketAvg > 0 && producerPrice > (marketAvg * 1.3)) {
            recommendation = "Advertencia: Tu precio es un 30% superior al promedio regional. Riesgo de baja rotación.";
            color = "WARNING";
        } else if (margin < 20) {
            recommendation = "Sugerencia: Tu margen de ganancia es menor al 20%. Revisa tus gastos operativos.";
            color = "WARNING";
        } else {
            recommendation = "Precio Saludable: Tu posición competitiva y margen son óptimos.";
            color = "SUCCESS";
        }

        return PriceAnalysisResponseDTO.builder()
                .unitCost(unitCost)
                .marketAverage(marketAvg)
                .marginPercentage(margin)
                .recommendation(recommendation)
                .statusColor(color)
                .build();
    }
}
