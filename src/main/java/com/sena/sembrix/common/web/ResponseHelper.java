package com.sena.sembrix.common.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

public class ResponseHelper {

    public static <T> ResponseEntity<ApiResponse<T>> ok(T data) {
        ApiResponse<T> resp = ApiResponse.<T>builder()
                .success(true)
                .message("OK")
                .data(data)
                .build();
        return ResponseEntity.ok(resp);
    }

    public static <T> ResponseEntity<ApiResponse<T>> created(T data) {
        ApiResponse<T> resp = ApiResponse.<T>builder()
                .success(true)
                .message("Created")
                .data(data)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    public static ResponseEntity<ApiError> error(HttpStatus status, String message) {
        ApiError err = ApiError.builder()
                .status(status)
                .message(message)
                .errors(Collections.emptyList())
                .build();
        return ResponseEntity.status(status).body(err);
    }

}

