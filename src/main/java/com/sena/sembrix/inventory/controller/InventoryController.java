package com.sena.sembrix.inventory.controller;

import com.sena.sembrix.common.pricing.dto.PriceAnalysisResponseDTO;
import com.sena.sembrix.common.pricing.service.PricingEngineService;
import com.sena.sembrix.common.web.ApiResponse;
import com.sena.sembrix.common.web.ResponseHelper;
import com.sena.sembrix.inventory.Inventory;
import com.sena.sembrix.inventory.dto.InventoryDto;
import com.sena.sembrix.inventory.service.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory")
@CrossOrigin(origins = "http://localhost:5173")
public class InventoryController {

    private final InventoryService service;
    private final PricingEngineService pricingEngineService;

    public InventoryController(InventoryService service, PricingEngineService pricingEngineService) {
        this.service = service;
        this.pricingEngineService = pricingEngineService;
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
    public ResponseEntity<?> getAll(@RequestParam(required = false) Long profileProducerId) {
        List<InventoryDto> list;
        if (profileProducerId != null) {
            list = service.findByProfileProducerId(profileProducerId);
        } else {
            list = service.findAll();
        }
        return ResponseHelper.ok(list);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody InventoryDto dto) {
        InventoryDto i = service.update(id, dto);
        return ResponseHelper.ok(i);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseHelper.ok(null);
    }

    @GetMapping("/{id}/analysis")
    public ResponseEntity<ApiResponse<PriceAnalysisResponseDTO>> getPriceAnalysis(@PathVariable Long id) {
        Inventory inventory = service.findEntityById(id); // Método que devuelve la Entidad, no el DTO
        PriceAnalysisResponseDTO analysis = pricingEngineService.analyzeInventoryPrice(inventory);
        return ResponseHelper.ok(analysis);
    }
}

