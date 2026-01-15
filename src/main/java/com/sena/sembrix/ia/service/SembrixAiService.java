package com.sena.sembrix.ia.service;

import com.sena.sembrix.ia.AiChatClient;
import com.sena.sembrix.ia.component.AiPromptBuilder;
import com.sena.sembrix.inventory.Inventory;
import com.sena.sembrix.inventory.repository.InventoryRepository;
import com.sena.sembrix.market.MarketPrice;
import com.sena.sembrix.market.repository.MarketPriceRepository;
import com.sena.sembrix.production.ProductionExpenseItem;
import com.sena.sembrix.production.repository.ProductionExpenseItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SembrixAiService {

    private final InventoryRepository inventoryRepository;
    private final ProductionExpenseItemRepository expenseRepository;
    private final MarketPriceRepository marketPriceRepository;
    private final AiPromptBuilder promptBuilder;
    private final AiChatClient aiChatClient; // Se inyectará la implementación activa

    public String getAiAdvice(Long inventoryId, String question) {
        // 1. Recuperar toda la información de la DB
        Inventory inv = inventoryRepository.findById(inventoryId).orElseThrow();
        List<ProductionExpenseItem> expenses = expenseRepository.findByInventoryId(inventoryId);
        MarketPrice market = marketPriceRepository.findByProductIdAndRegion(
                inv.getProduct().getId(), inv.getProfileProducer().getRegion()
        ).orElse(null);

        // 2. Construir el prompt con datos reales
        String fullPrompt = promptBuilder.buildContextualPrompt(inv, expenses, market, question);

        // 3. Obtener respuesta de la IA externa
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