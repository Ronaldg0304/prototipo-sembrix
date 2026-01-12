package com.sena.sembrix.inventory.controller;

import com.sena.sembrix.common.web.ResponseHelper;
import com.sena.sembrix.inventory.dto.ProductDto;
import com.sena.sembrix.inventory.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ProductDto dto) {
        ProductDto created = service.create(dto);
        return ResponseHelper.created(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        ProductDto p = service.findById(id);
        return ResponseHelper.ok(p);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<?> getByName(@PathVariable String name) {
        ProductDto p = service.findByName(name);
        return ResponseHelper.ok(p);
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<ProductDto> list = service.findAll();
        return ResponseHelper.ok(list);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ProductDto dto) {
        ProductDto p = service.update(id, dto);
        return ResponseHelper.ok(p);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseHelper.ok(null);
    }
}

