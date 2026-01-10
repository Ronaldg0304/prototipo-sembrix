package com.sena.sembrix.inventory.controller;

import com.sena.sembrix.common.web.ResponseHelper;
import com.sena.sembrix.inventory.dto.InventoryDto;
import com.sena.sembrix.inventory.service.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService service;

    public InventoryController(InventoryService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody InventoryDto dto) {
        InventoryDto created = service.create(dto);
        return ResponseHelper.created(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        InventoryDto i = service.findById(id);
        return ResponseHelper.ok(i);
    }

    @GetMapping("/producer/{profileProducerId}")
    public ResponseEntity<?> getByProfileProducerId(@PathVariable Long profileProducerId) {
        List<InventoryDto> list = service.findByProfileProducerId(profileProducerId);
        return ResponseHelper.ok(list);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<?> getByProductId(@PathVariable Long productId) {
        List<InventoryDto> list = service.findByProductId(productId);
        return ResponseHelper.ok(list);
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<InventoryDto> list = service.findAll();
        return ResponseHelper.ok(list);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseHelper.ok(null);
    }
}

