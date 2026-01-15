package com.sena.sembrix.production.controller;

import com.sena.sembrix.common.web.ResponseHelper;
import com.sena.sembrix.production.dto.ProductionExpenseItemDto;
import com.sena.sembrix.production.service.ProductionExpenseItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/production-expense-items")
@CrossOrigin(origins = "http://localhost:5173")
public class ProductionExpenseItemController {

    private final ProductionExpenseItemService service;

    public ProductionExpenseItemController(ProductionExpenseItemService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ProductionExpenseItemDto dto) {
        ProductionExpenseItemDto created = service.create(dto);
        return ResponseHelper.created(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        ProductionExpenseItemDto pei = service.findById(id);
        return ResponseHelper.ok(pei);
    }

    @GetMapping("/expense/{productionExpenseId}")
    public ResponseEntity<?> getByProductionExpenseId(@PathVariable Long productionExpenseId) {
        List<ProductionExpenseItemDto> list = service.findByProductionExpenseId(productionExpenseId);
        return ResponseHelper.ok(list);
    }

    @GetMapping("/inventory/{inventoryId}")
    public ResponseEntity<?> getByInventoryId(@PathVariable Long inventoryId) {
        List<ProductionExpenseItemDto> list = service.findByInventoryId(inventoryId);
        return ResponseHelper.ok(list);
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<ProductionExpenseItemDto> list = service.findAll();
        return ResponseHelper.ok(list);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseHelper.ok(null);
    }
}

