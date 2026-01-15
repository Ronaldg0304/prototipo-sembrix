package com.sena.sembrix.market.controller;

import com.sena.sembrix.common.web.ResponseHelper;
import com.sena.sembrix.market.dto.MarketPriceDto;
import com.sena.sembrix.market.service.MarketPriceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/market-prices")
@CrossOrigin(origins = "http://localhost:5173")
public class MarketPriceController {

    private final MarketPriceService service;

    public MarketPriceController(MarketPriceService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody MarketPriceDto dto) {
        MarketPriceDto created = service.create(dto);
        return ResponseHelper.created(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        MarketPriceDto mp = service.findById(id);
        return ResponseHelper.ok(mp);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<?> getByProductId(@PathVariable Long productId) {
        List<MarketPriceDto> list = service.findByProductId(productId);
        return ResponseHelper.ok(list);
    }

    @GetMapping("/region/{region}")
    public ResponseEntity<?> getByRegion(@PathVariable String region) {
        List<MarketPriceDto> list = service.findByRegion(region);
        return ResponseHelper.ok(list);
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<MarketPriceDto> list = service.findAll();
        return ResponseHelper.ok(list);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody MarketPriceDto dto) {
        MarketPriceDto updated = service.update(id, dto);
        return ResponseHelper.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseHelper.ok(null);
    }
}

