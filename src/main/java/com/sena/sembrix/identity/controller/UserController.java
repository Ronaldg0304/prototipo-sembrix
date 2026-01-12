package com.sena.sembrix.identity.controller;

import com.sena.sembrix.common.web.ResponseHelper;
import com.sena.sembrix.identity.dto.UserRequestDto;
import com.sena.sembrix.identity.dto.UserResponseDto;
import com.sena.sembrix.identity.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody UserRequestDto dto) {
        UserResponseDto created = userService.create(dto);
        return ResponseHelper.created(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        UserResponseDto u = userService.findById(id);
        return ResponseHelper.ok(u);
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<UserResponseDto> list = userService.findAll();
        return ResponseHelper.ok(list);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UserRequestDto dto) {
        UserResponseDto u = userService.update(id, dto);
        return ResponseHelper.ok(u);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseHelper.ok(null);
    }
}
