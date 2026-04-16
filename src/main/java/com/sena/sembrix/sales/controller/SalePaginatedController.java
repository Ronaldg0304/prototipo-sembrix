package com.sena.sembrix.sales.controller;

import com.sena.sembrix.common.web.ApiResponse;
import com.sena.sembrix.common.web.PagedResponse;
import com.sena.sembrix.sales.dto.SaleDto;
import com.sena.sembrix.sales.service.SaleService;
import com.sena.sembrix.identity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * Sales Controller con soporte de paginación
 * Proporciona endpoints para gestionar ventas con búsqueda paginada
 */
@RestController
@RequestMapping("/api/v1/sales")
@RequiredArgsConstructor
public class SalePaginatedController {

    private final SaleService saleService;

    /**
     * GET /api/v1/sales/paginated
     *
     * Obtiene todas las ventas con paginación (solo para administradores)
     *
     * @param page Número de página (0-indexed), default: 0
     * @param size Tamaño de página, default: 10
     * @param sortBy Campo para ordenar, default: "saleDate"
     * @param sortDir Dirección (asc/desc), default: "desc"
     *
     * @return Respuesta paginada con DTOs de venta
     */
    @GetMapping("/paginated")
    public ResponseEntity<ApiResponse<PagedResponse<SaleDto>>> getAllSalesPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "saleDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        PagedResponse<SaleDto> response = saleService.findAllPaginated(page, size, sortBy, sortDir);

        return ResponseEntity.ok(ApiResponse.<PagedResponse<SaleDto>>builder()
                .success(true)
                .message("Sales retrieved successfully")
                .data(response)
                .build());
    }

    /**
     * GET /api/v1/sales/by-producer/paginated
     *
     * Obtiene ventas por productor con paginación
     *
     * @param profileProducerId ID del productor
     * @param page Número de página (0-indexed), default: 0
     * @param size Tamaño de página, default: 10
     * @param sortBy Campo para ordenar, default: "saleDate"
     * @param sortDir Dirección (asc/desc), default: "desc"
     *
     * @return Respuesta paginada con ventas del productor
     */
    @GetMapping("/by-producer/{profileProducerId}/paginated")
    public ResponseEntity<ApiResponse<PagedResponse<SaleDto>>> getSalesByProducerPaginated(
            @PathVariable Long profileProducerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "saleDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        PagedResponse<SaleDto> response = saleService.findByProfileProducerIdPaginated(
                profileProducerId, page, size, sortBy, sortDir);

        return ResponseEntity.ok(com.sena.sembrix.common.web.ApiResponse.<PagedResponse<SaleDto>>builder()
                .success(true)
                .message("Producer sales retrieved successfully")
                .data(response)
                .build());
    }

    /**
     * GET /api/v1/sales/my-sales/paginated
     *
     * Obtiene ventas del productor autenticado con paginación
     *
     * @param currentUser Usuario autenticado
     * @param page Número de página (0-indexed), default: 0
     * @param size Tamaño de página, default: 10
     * @param sortBy Campo para ordenar, default: "saleDate"
     * @param sortDir Dirección (asc/desc), default: "desc"
     *
     * @return Respuesta paginada con las ventas del usuario autenticado
     */
    @GetMapping("/my-sales/paginated")
    public ResponseEntity<ApiResponse<PagedResponse<SaleDto>>> getMyCurrentUserSalesPaginated(
            @AuthenticationPrincipal UserEntity currentUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "saleDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        PagedResponse<SaleDto> response = saleService.findByProfileProducerIdPaginated(
                currentUser.getId(), page, size, sortBy, sortDir);

        return ResponseEntity.ok(com.sena.sembrix.common.web.ApiResponse.<PagedResponse<SaleDto>>builder()
                .success(true)
                .message("Your sales retrieved successfully")
                .data(response)
                .build());
    }
}
