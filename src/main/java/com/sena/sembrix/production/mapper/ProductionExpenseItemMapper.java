package com.sena.sembrix.production.mapper;

import com.sena.sembrix.inventory.Inventory;
import com.sena.sembrix.production.ExpenseCategory;
import com.sena.sembrix.production.ProductionExpense;
import com.sena.sembrix.production.ProductionExpenseItem;
import com.sena.sembrix.production.dto.ProductionExpenseItemDto;
import com.sena.sembrix.inventory.repository.InventoryRepository;
import com.sena.sembrix.production.repository.ProductionExpenseRepository;
import org.springframework.stereotype.Component;

@Component
public class ProductionExpenseItemMapper {

    private final InventoryRepository inventoryRepository;
    private final ProductionExpenseRepository productionExpenseRepository;

    public ProductionExpenseItemMapper(InventoryRepository inventoryRepository, ProductionExpenseRepository productionExpenseRepository) {
        this.inventoryRepository = inventoryRepository;
        this.productionExpenseRepository = productionExpenseRepository;
    }

    public ProductionExpenseItem toEntity(ProductionExpenseItemDto dto) {
        if (dto == null) return null;
        ProductionExpenseItem e = new ProductionExpenseItem();
        e.setDescription(dto.getDescription());
        e.setAmount(dto.getAmount());
        e.setExpenseDate(dto.getExpenseDate());

        if (dto.getCategory() != null) {
            try { e.setCategory(ExpenseCategory.valueOf(dto.getCategory())); } catch (Exception ex) { e.setCategory(ExpenseCategory.OTHER); }
        } else {
            e.setCategory(ExpenseCategory.OTHER);
        }

        if (dto.getInventoryId() != null) {
            Inventory inv = inventoryRepository.findById(dto.getInventoryId()).orElse(null);
            e.setInventory(inv);
        }

        if (dto.getProductionExpenseId() != null) {
            ProductionExpense pe = productionExpenseRepository.findById(dto.getProductionExpenseId()).orElse(null);
            e.setProductionExpense(pe);
        }
        return e;
    }

    public ProductionExpenseItemDto toDto(ProductionExpenseItem entity) {
        if (entity == null) return null;
        ProductionExpenseItemDto dto = new ProductionExpenseItemDto();
        dto.setId(entity.getId());
        dto.setDescription(entity.getDescription());
        dto.setAmount(entity.getAmount());
        dto.setExpenseDate(entity.getExpenseDate());
        dto.setCategory(entity.getCategory() != null ? entity.getCategory().name() : null);
        if (entity.getInventory() != null) {
            dto.setInventoryId(entity.getInventory().getId());
        }
        if (entity.getProductionExpense() != null) {
            dto.setProductionExpenseId(entity.getProductionExpense().getId());
        }
        return dto;
    }
}

