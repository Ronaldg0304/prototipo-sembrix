package com.sena.sembrix.sales.controller;

import com.sena.sembrix.common.web.ResponseHelper;
import com.sena.sembrix.inventory.dto.InventoryDto;
import com.sena.sembrix.sales.dto.CustomerDto;
import com.sena.sembrix.sales.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@CrossOrigin(origins = "http://localhost:5173")
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
    public ResponseEntity<?> getAll(@RequestParam(required = false) Long profileProducerId) {
        List<CustomerDto> list;
        if (profileProducerId != null) {
            list = service.findByProfileProducerId(profileProducerId);
        } else {
            list = service.findAll();
        }
        return ResponseHelper.ok(list);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody CustomerDto dto) {
        CustomerDto c = service.update(id, dto);
        return ResponseHelper.ok(c);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseHelper.ok(null);
    }
}

