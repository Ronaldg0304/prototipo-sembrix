package com.sena.sembrix.identity.controller;

import com.sena.sembrix.common.web.ResponseHelper;
import com.sena.sembrix.identity.dto.UserRequestDto;
import com.sena.sembrix.identity.dto.UserResponseDto;
import com.sena.sembrix.identity.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/users")
@CrossOrigin(origins = "http://localhost:5173")
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
    public ResponseEntity<com.sena.sembrix.common.web.ApiResponse<com.sena.sembrix.common.web.PagedResponse<UserResponseDto>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        com.sena.sembrix.common.web.PagedResponse<UserResponseDto> response = userService.findAllPaginated(page, size, sortBy, sortDir);
        return ResponseEntity.ok(com.sena.sembrix.common.web.ApiResponse.<com.sena.sembrix.common.web.PagedResponse<UserResponseDto>>builder()
                .success(true)
                .message("Users retrieved successfully")
                .data(response)
                .build());
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
