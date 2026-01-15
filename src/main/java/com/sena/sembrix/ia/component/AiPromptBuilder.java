package com.sena.sembrix.ia.component;

import com.sena.sembrix.inventory.Inventory;
import com.sena.sembrix.market.MarketPrice;
import com.sena.sembrix.production.ProductionExpenseItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AiPromptBuilder {

    public String buildContextualPrompt(Inventory inventory, List<ProductionExpenseItem> expenses, MarketPrice market, String userQuestion) {
        double totalExpenses = expenses.stream().mapToDouble(ProductionExpenseItem::getAmount).sum();

        return String.format(
                """
                        Eres el Asesor Inteligente de Sembrix, una plataforma agrícola. Tu objetivo es ayudar al productor con datos reales.
                        
                        CONTEXTO DEL PRODUCTOR:
                        - Producto: %s
                        - Ubicación: %s
                        - Stock actual: %d unidades
                        - Precio fijado por el productor: $%.2f
                        - Gastos totales registrados hasta hoy: $%.2f
                        
                        DATOS DE MERCADO REGIONAL:
                        - Precio promedio en la zona: $%.2f
                        - Tendencia: %s
                        
                        PREGUNTA DEL USUARIO: "%s"
                        
                        INSTRUCCIONES: Responde de forma clara, técnica pero cercana al agricultor. Si el precio del productor es mayor al del mercado, adviértele. Si tiene pérdidas, sugiérele revisar costos.""",
                inventory.getProduct().getName(),
                inventory.getProfileProducer().getRegion(),
                inventory.getCurrentStock().intValue(),
                inventory.getUnitPrice(),
                totalExpenses,
                market != null ? market.getAverageMarketPrice() : 0,
                market != null ? market.getTrend() : "Estable",
                userQuestion
        );
    }

    public String buildGeneralContextualPrompt(List<Inventory> inventories, String userQuestion) {
        StringBuilder inventorySummary = new StringBuilder();
        for (Inventory inv : inventories) {
            inventorySummary.append(String.format(
                "- %s: Stock %d, Precio $%.2f\n",
                inv.getProduct().getName(),
                inv.getCurrentStock().intValue(),
                inv.getUnitPrice()
            ));
        }

        return String.format(
                """
                        Eres el Asesor Inteligente de Sembrix. Se te ha proporcionado un resumen de TODO el inventario del productor.
                        
                        RESUMEN DE INVENTARIO:
                        %s
                        
                        PREGUNTA DEL USUARIO: "%s"
                        
                        INSTRUCCIONES: Analiza el panorama general. Si tiene muchos productos, ofrece consejos de diversificación o rotación. Si algunos tienen stock bajo, recuérdaselo. Responde de forma técnica pero accesible en español.""",
                inventorySummary.toString(),
                userQuestion
        );
    }
}
