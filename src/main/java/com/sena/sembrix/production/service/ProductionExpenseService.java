package com.sena.sembrix.production.service;

import com.sena.sembrix.production.dto.ProductionExpenseDto;

import java.util.List;

public interface ProductionExpenseService {
    ProductionExpenseDto create(ProductionExpenseDto dto);
    ProductionExpenseDto findById(Long id);
    List<ProductionExpenseDto> findAll();
    void delete(Long id);
}

