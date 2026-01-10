package com.sena.sembrix.identity.controller;

import com.sena.sembrix.common.web.ResponseHelper;
import com.sena.sembrix.identity.dto.ProfileProducerDto;
import com.sena.sembrix.identity.service.ProfileProducerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/producers")
public class ProfileProducerController {

    private final ProfileProducerService service;

    public ProfileProducerController(ProfileProducerService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ProfileProducerDto dto) {
        ProfileProducerDto created = service.create(dto);
        return ResponseHelper.created(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        ProfileProducerDto p = service.findById(id);
        return ResponseHelper.ok(p);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getByUserId(@PathVariable Long userId) {
        ProfileProducerDto p = service.findByUserId(userId);
        return ResponseHelper.ok(p);
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<ProfileProducerDto> list = service.findAll();
        return ResponseHelper.ok(list);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseHelper.ok(null);
    }
}

