package com.sena.sembrix.system.controller;

import com.sena.sembrix.identity.UserEntity;
import com.sena.sembrix.system.SystemSetting;
import com.sena.sembrix.system.service.SystemSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/settings")
@RequiredArgsConstructor
public class SystemSettingController {

    private final SystemSettingService service;

    @GetMapping
    public ResponseEntity<List<SystemSetting>> getAllSettings(Authentication authentication) {
        UserEntity user = (UserEntity) authentication.getPrincipal();
        return ResponseEntity.ok(service.getAllSettings(user));
    }

    @PutMapping
    public ResponseEntity<List<SystemSetting>> updateSettings(@RequestBody Map<String, String> updates, Authentication authentication) {
        UserEntity user = (UserEntity) authentication.getPrincipal();
        return ResponseEntity.ok(service.updateSettings(user, updates));
    }
}
