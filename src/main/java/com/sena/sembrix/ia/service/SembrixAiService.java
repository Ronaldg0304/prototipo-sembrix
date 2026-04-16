package com.sena.sembrix.ia.service;

import com.sena.sembrix.ia.AiChatClient;
import com.sena.sembrix.ia.component.AiPromptBuilder;
import com.sena.sembrix.inventory.Inventory;
import com.sena.sembrix.inventory.repository.InventoryRepository;
import com.sena.sembrix.market.domain.MarketData;
import com.sena.sembrix.market.domain.MarketPrice;
import com.sena.sembrix.market.infrastructure.repository.MarketDataRepository;
import com.sena.sembrix.market.infrastructure.repository.MarketPriceRepository;
import com.sena.sembrix.production.Harvest;
import com.sena.sembrix.production.ProductionExpenseItem;
import com.sena.sembrix.production.repository.HarvestRepository;
import com.sena.sembrix.production.repository.ProductionExpenseItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SembrixAiService {

    private final InventoryRepository inventoryRepository;
    private final ProductionExpenseItemRepository expenseRepository;
    private final MarketPriceRepository marketPriceRepository;
    private final MarketDataRepository marketDataRepository;
    private final HarvestRepository harvestRepository;
    private final AiPromptBuilder promptBuilder;
    private final AiChatClient aiChatClient; 

    public String getAiAdvice(Long inventoryId, String question) {
        // 1. Recuperar información base
        Inventory inv = inventoryRepository.findById(inventoryId).orElseThrow();
        Long productId = inv.getProduct().getId();
        String region = inv.getProfileProducer().getRegion();
        Long producerId = inv.getProfileProducer().getId();

        // 2. Cost Context: Find most recent Harvest to get total expenses
        Harvest latestHarvest = harvestRepository.findMostRecentByProducerAndProduct(producerId, productId)
                .stream().findFirst().orElse(null);
        BigDecimal productionCost = latestHarvest != null ? latestHarvest.getTotalExpenses() : BigDecimal.ZERO;

        // 3. Market Context: Match product and region (case-insensitive)
        MarketPrice market = marketPriceRepository.findByProductIdAndRegionIgnoreCase(productId, region).orElse(null);
        
        Double marketPrice = 0.0;
        String trend = "STABLE";

        if (market != null && market.getAverageMarketPrice() != null && market.getAverageMarketPrice() > 0) {
            marketPrice = market.getAverageMarketPrice();
            trend = market.getTrend() != null ? market.getTrend() : "STABLE";
        } else {
            // Fallback: Query market_data for the most recent entry in that region
            List<MarketData> fallbackData = marketDataRepository.findLatestByRegion(region);
            if (!fallbackData.isEmpty()) {
                marketPrice = fallbackData.get(0).getExternalPrice();
                trend = "HISTORY_BASED";
            }
        }

        // 4. Construir el prompt y obtener respuesta
        String fullPrompt = promptBuilder.buildContextualPromptV2(
            inv, 
            productionCost, 
            marketPrice, 
            trend, 
            question
        );

        return aiChatClient.generateResponse(fullPrompt);
    }

    public String getGeneralAdvice(Long profileId, String question) {
        // 1. Recuperar todo el inventario del productor
        List<Inventory> inventories = inventoryRepository.findByProfileProducerId(profileId);
        
        // 2. Construir prompt general
        String fullPrompt = promptBuilder.buildGeneralContextualPrompt(inventories, question);
        
        // 3. Obtener respuesta
        return aiChatClient.generateResponse(fullPrompt);
    }
}