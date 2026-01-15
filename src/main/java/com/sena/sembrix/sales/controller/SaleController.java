package com.sena.sembrix.sales.controller;

import com.sena.sembrix.common.web.ResponseHelper;
import com.sena.sembrix.sales.dto.SaleDto;
import com.sena.sembrix.sales.service.SaleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sales")
@CrossOrigin(origins = "http://localhost:5173")
public class SaleController {

    private final SaleService service;

    public SaleController(SaleService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody SaleDto dto) {
        SaleDto created = service.create(dto);
        return ResponseHelper.created(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        SaleDto s = service.findById(id);
        return ResponseHelper.ok(s);
    }

    @GetMapping("/producer/{profileProducerId}")
    public ResponseEntity<?> getByProfileProducerId(@PathVariable Long profileProducerId) {
        List<SaleDto> list = service.findByProfileProducerId(profileProducerId);
        return ResponseHelper.ok(list);
    }

    @GetMapping("/invoice/{invoiceId}")
    public ResponseEntity<?> getByInvoiceId(@PathVariable Long invoiceId) {
        SaleDto s = service.findByInvoiceId(invoiceId);
        return ResponseHelper.ok(s);
    }

    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam(required = false) Long profileProducerId) {
        List<SaleDto> list;
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

