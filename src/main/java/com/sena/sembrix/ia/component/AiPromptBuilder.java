package com.sena.sembrix.ia.component;

import com.sena.sembrix.inventory.Inventory;
import com.sena.sembrix.market.domain.MarketPrice;
import com.sena.sembrix.production.ProductionExpenseItem;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AiPromptBuilder {

    public String buildContextualPromptV2(Inventory inventory, BigDecimal productionCost, Double marketPrice, String marketTrend, String userQuestion) {
        return String.format(
                """
Eres el Asesor Económico de Sembrix. Genera un REPORTE EJECUTIVO para el productor.

DATOS CLAVE:
- PRODUCTO: %s
- REGIÓN: %s
- PRECIO VENTA: $%.2f
- MERCADO (ZONA): $%.2f
- TENDENCIA: %s
- COSTO UNITARIO: $%.2f

PREGUNTA: "%s"

INSTRUCCIONES DE FORMATO (ESTRICTO):
1. Usa el siguiente esquema Markdown exactamente (SIN espacios al inicio de las líneas):
### 📊 RESUMEN: %s
---
**ANÁLISIS DE PRECIOS**
- [Diferencia porcentual vs mercado en 1 frase]
---
**RENTABILIDAD**
- [Margen unitario y total stock en 1 frase]
---
**ESTRATEGIA RECOMENDADA**
- [Consejo basado en la tendencia %s en máximo 2 frases]

2. NO saludes, NO digas "Estimado usuario", ve directo al reporte.
3. NO uses espacios al inicio de las líneas.
4. Responde en español.""",
                inventory.getProduct().getName(),
                inventory.getProfileProducer().getRegion(),
                inventory.getUnitPrice(),
                marketPrice,
                marketTrend,
                productionCost.doubleValue() / inventory.getCurrentStock().doubleValue(),
                userQuestion,
                inventory.getProduct().getName(),
                marketTrend
        );
    }

    public String buildContextualPrompt(Inventory inventory, List<ProductionExpenseItem> expenses, MarketPrice market, String userQuestion) {
        double totalExpenses = expenses.stream().mapToDouble(item -> item.getAmount().doubleValue()).sum();

        return String.format(
                """
Eres el Asesor Económico de Sembrix. Reporte rápido.

### 📊 REPORTE: %s
---
**PANORAMA ACTUAL**
- Stock: %d unidades
- Precio fijado: $%.2f
- Gastos: $%.2f
---
**MERCADO**
- Precio promedio: $%.2f
- Tendencia: %s
---
**ESTRATEGIA**
- [Breve consejo técnico basado en datos]

PREGUNTA: "%s" """,
                inventory.getProduct().getName(),
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
                "* **%s**: %d un. ($%.2f)\n",
                inv.getProduct().getName(),
                inv.getCurrentStock().intValue(),
                inv.getUnitPrice()
            ));
        }

        return String.format(
                """
Eres el Asesor Económico de Sembrix. Panorama general.

### 📋 RESUMEN INVENTARIO
---
**EXISTENCIAS**
%s
---
**CONSEJO GENERAL**
- [Breve análisis de rotación o diversificación]

PREGUNTA: "%s" """,
                inventorySummary.toString(),
                userQuestion
        );
    }
}
