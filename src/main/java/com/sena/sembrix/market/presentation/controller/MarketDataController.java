package com.sena.sembrix.market.presentation.controller;

import com.sena.sembrix.common.web.ApiResponse;
import com.sena.sembrix.common.web.PagedResponse;
import com.sena.sembrix.common.web.ResponseHelper;
import com.sena.sembrix.inventory.dto.InventoryDto;
import com.sena.sembrix.market.application.dto.MarketDataDto;
import com.sena.sembrix.market.application.service.MarketDataService;
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

    @GetMapping("/paginated")
    public ResponseEntity<ApiResponse<PagedResponse<MarketDataDto>>> getAllInventoriesPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        PagedResponse<MarketDataDto> response = service.findAllPaginated(page, size, sortBy, sortDir);

        return ResponseEntity.ok(ApiResponse.<PagedResponse<MarketDataDto>>builder()
                .success(true)
                .message("Market data retrieved successfully")
                .data(response)
                .build());
    }
}

