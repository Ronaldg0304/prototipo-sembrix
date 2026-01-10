package com.sena.sembrix.sales.controller;

import com.sena.sembrix.common.web.ResponseHelper;
import com.sena.sembrix.sales.dto.CustomerDto;
import com.sena.sembrix.sales.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CustomerDto dto) {
        CustomerDto created = service.create(dto);
        return ResponseHelper.created(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        CustomerDto c = service.findById(id);
        return ResponseHelper.ok(c);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<?> getByEmail(@PathVariable String email) {
        CustomerDto c = service.findByEmail(email);
        return ResponseHelper.ok(c);
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<CustomerDto> list = service.findAll();
        return ResponseHelper.ok(list);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseHelper.ok(null);
    }
}

