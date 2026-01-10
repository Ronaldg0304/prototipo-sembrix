package com.sena.sembrix.security.controller;

import com.sena.sembrix.common.web.ApiResponse;
import com.sena.sembrix.common.web.ResponseHelper;
import com.sena.sembrix.security.dto.AuthenticationRequest;
import com.sena.sembrix.security.dto.AuthenticationResponse;
import com.sena.sembrix.security.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;


    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

//    @PostMapping("/register")
//    public ResponseEntity<ApiResponse<?>> register(@Valid @RequestBody RegisterRequest registerRequest) {
//        AuthenticationResponse res = authenticationService.register(registerRequest);
//        return ResponseEntity.ok(ApiResponseBuilder.success(res, HttpStatus.CREATED));
//    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
        AuthenticationResponse res = authenticationService.authenticate(authenticationRequest);
        return ResponseEntity.ok(ResponseHelper.ok(res).getBody());
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refresh(@RequestParam("token") String refreshToken) {
        try {
            AuthenticationResponse res = authenticationService.refreshToken(refreshToken);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @GetMapping("/validateToken")
    public ResponseEntity<Boolean> validateToken(@RequestParam("token") String token) {
        try {
            Boolean res = authenticationService.validateToken(token);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



}