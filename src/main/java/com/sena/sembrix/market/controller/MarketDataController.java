package com.sena.sembrix.market.controller;

import com.sena.sembrix.common.web.ResponseHelper;
import com.sena.sembrix.market.dto.MarketDataDto;
import com.sena.sembrix.market.service.MarketDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/market-data")
@CrossOrigin(origins = "http://localhost:5173")
public class MarketDataController {

    private final MarketDataService service;

    public MarketDataController(MarketDataService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody MarketDataDto dto) {
        MarketDataDto created = service.create(dto);
        return ResponseHelper.created(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        MarketDataDto md = service.findById(id);
        return ResponseHelper.ok(md);
    }

    @GetMapping("/source/{sourceApiName}")
    public ResponseEntity<?> getBySourceApiName(@PathVariable String sourceApiName) {
        List<MarketDataDto> list = service.findBySourceApiName(sourceApiName);
        return ResponseHelper.ok(list);
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<MarketDataDto> list = service.findAll();
        return ResponseHelper.ok(list);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseHelper.ok(null);
    }
}

