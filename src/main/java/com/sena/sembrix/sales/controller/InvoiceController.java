package com.sena.sembrix.sales.controller;

import com.sena.sembrix.common.web.ResponseHelper;
import com.sena.sembrix.sales.dto.InvoiceDto;
import com.sena.sembrix.sales.service.InvoiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceService service;

    public InvoiceController(InvoiceService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody InvoiceDto dto) {
        InvoiceDto created = service.create(dto);
        return ResponseHelper.created(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        InvoiceDto inv = service.findById(id);
        return ResponseHelper.ok(inv);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<?> getByCustomerId(@PathVariable Long customerId) {
        List<InvoiceDto> list = service.findByCustomerId(customerId);
        return ResponseHelper.ok(list);
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<InvoiceDto> list = service.findAll();
        return ResponseHelper.ok(list);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseHelper.ok(null);
    }
}

