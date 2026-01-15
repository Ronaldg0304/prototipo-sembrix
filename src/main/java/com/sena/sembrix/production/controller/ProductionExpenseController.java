package com.sena.sembrix.production.controller;

import com.sena.sembrix.common.web.ResponseHelper;
import com.sena.sembrix.production.dto.ProductionExpenseDto;
import com.sena.sembrix.production.service.ProductionExpenseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/production-expenses")
@CrossOrigin(origins = "http://localhost:5173")
public class ProductionExpenseController {

    private final ProductionExpenseService service;

    public ProductionExpenseController(ProductionExpenseService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ProductionExpenseDto dto) {
        ProductionExpenseDto created = service.create(dto);
        return ResponseHelper.created(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        ProductionExpenseDto pe = service.findById(id);
        return ResponseHelper.ok(pe);
    }

    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam(required = false) Long profileProducerId) {
        List<ProductionExpenseDto> list;
        if (profileProducerId != null) {
            list = service.findByProfileProducerId(profileProducerId);
        } else {
            list = service.findAll();
        }
        return ResponseHelper.ok(list);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseHelper.ok(null);
    }
}

