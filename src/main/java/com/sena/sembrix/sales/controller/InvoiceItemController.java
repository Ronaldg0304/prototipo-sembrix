package com.sena.sembrix.sales.controller;

import com.sena.sembrix.common.web.ResponseHelper;
import com.sena.sembrix.sales.dto.InvoiceItemDto;
import com.sena.sembrix.sales.service.InvoiceItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/invoice-items")
@CrossOrigin(origins = "http://localhost:5173")
public class InvoiceItemController {

    private final InvoiceItemService service;

    public InvoiceItemController(InvoiceItemService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody InvoiceItemDto dto) {
        InvoiceItemDto created = service.create(dto);
        return ResponseHelper.created(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        InvoiceItemDto ii = service.findById(id);
        return ResponseHelper.ok(ii);
    }

    @GetMapping("/invoice/{invoiceId}")
    public ResponseEntity<?> getByInvoiceId(@PathVariable Long invoiceId) {
        List<InvoiceItemDto> list = service.findByInvoiceId(invoiceId);
        return ResponseHelper.ok(list);
    }

    @GetMapping("/inventory/{inventoryId}")
    public ResponseEntity<?> getByInventoryId(@PathVariable Long inventoryId) {
        List<InvoiceItemDto> list = service.findByInventoryId(inventoryId);
        return ResponseHelper.ok(list);
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<InvoiceItemDto> list = service.findAll();
        return ResponseHelper.ok(list);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseHelper.ok(null);
    }
}

