package com.sena.sembrix.inventory.controller;

import com.sena.sembrix.common.web.ApiResponse;
import com.sena.sembrix.common.web.PagedResponse;
import com.sena.sembrix.inventory.dto.InventoryDto;
import com.sena.sembrix.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Inventory Controller con soporte de paginación
 * Proporciona endpoints para gestionar inventarios con búsqueda paginada
 */
@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
public class InventoryPaginatedController {

    private final InventoryService inventoryService;

    /**
     * GET /api/v1/inventory/paginated
     *
     * Obtiene todos los inventarios con paginación
     *
     * @param page Número de página (0-indexed), default: 0
     * @param size Tamaño de página, default: 10
     * @param sortBy Campo para ordenar, default: "createdAt"
     * @param sortDir Dirección (asc/desc), default: "desc"
     *
     * @return Respuesta paginada con DTOs de inventario
     */
    @GetMapping("/paginated")
    public ResponseEntity<ApiResponse<PagedResponse<InventoryDto>>> getAllInventoriesPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        PagedResponse<InventoryDto> response = inventoryService.findAllPaginated(page, size, sortBy, sortDir);

        return ResponseEntity.ok(ApiResponse.<PagedResponse<InventoryDto>>builder()
                .success(true)
                .message("Inventories retrieved successfully")
                .data(response)
                .build());
    }

    /**
     * GET /api/v1/inventory/by-producer/paginated
     *
     * Obtiene inventarios por productor con paginación
     *
     * @param profileProducerId ID del productor
     * @param page Número de página (0-indexed), default: 0
     * @param size Tamaño de página, default: 10
     * @param sortBy Campo para ordenar, default: "createdAt"
     * @param sortDir Dirección (asc/desc), default: "desc"
     *
     * @return Respuesta paginada con inventarios del productor
     */
    @GetMapping("/by-producer/{profileProducerId}/paginated")
    public ResponseEntity<ApiResponse<PagedResponse<InventoryDto>>> getInventoriesByProducerPaginated(
            @PathVariable Long profileProducerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        PagedResponse<InventoryDto> response = inventoryService.findByProfileProducerIdPaginated(
                profileProducerId, page, size, sortBy, sortDir);

        return ResponseEntity.ok(com.sena.sembrix.common.web.ApiResponse.<PagedResponse<InventoryDto>>builder()
                .success(true)
                .message("Producer inventories retrieved successfully")
                .data(response)
                .build());
    }

    /**
     * GET /api/v1/inventory/by-product/paginated
     *
     * Obtiene inventarios por producto con paginación
     *
     * @param productId ID del producto
     * @param page Número de página (0-indexed), default: 0
     * @param size Tamaño de página, default: 10
     * @param sortBy Campo para ordenar, default: "createdAt"
     * @param sortDir Dirección (asc/desc), default: "desc"
     *
     * @return Respuesta paginada con inventarios del producto
     */
    @GetMapping("/by-product/{productId}/paginated")
    public ResponseEntity<ApiResponse<PagedResponse<InventoryDto>>> getInventoriesByProductPaginated(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        PagedResponse<InventoryDto> response = inventoryService.findByProductIdPaginated(
                productId, page, size, sortBy, sortDir);

        return ResponseEntity.ok(com.sena.sembrix.common.web.ApiResponse.<PagedResponse<InventoryDto>>builder()
                .success(true)
                .message("Product inventories retrieved successfully")
                .data(response)
                .build());
    }
}
