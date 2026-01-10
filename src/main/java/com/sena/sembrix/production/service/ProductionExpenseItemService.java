package com.sena.sembrix.production.service;

import com.sena.sembrix.production.dto.ProductionExpenseItemDto;

import java.util.List;

public interface ProductionExpenseItemService {
    ProductionExpenseItemDto create(ProductionExpenseItemDto dto);
    ProductionExpenseItemDto findById(Long id);
    List<ProductionExpenseItemDto> findByProductionExpenseId(Long productionExpenseId);
    List<ProductionExpenseItemDto> findByInventoryId(Long inventoryId);
    List<ProductionExpenseItemDto> findAll();
    void delete(Long id);
}

