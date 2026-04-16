package com.sena.sembrix.sales.controller;

import com.sena.sembrix.common.web.ApiResponse;
import com.sena.sembrix.common.web.PagedResponse;
import com.sena.sembrix.sales.dto.InvoiceDto;
import com.sena.sembrix.sales.service.InvoiceService;
import com.sena.sembrix.identity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * Invoice Controller con soporte de paginación
 * Proporciona endpoints para gestionar facturas con búsqueda paginada
 */
@RestController
@RequestMapping("/api/v1/invoices")
@RequiredArgsConstructor
public class InvoicePaginatedController {

    private final InvoiceService invoiceService;

    /**
     * GET /api/v1/invoices/paginated
     *
     * Obtiene todas las facturas con paginación
     *
     * @param page Número de página (0-indexed), default: 0
     * @param size Tamaño de página, default: 10
     * @param sortBy Campo para ordenar, default: "date"
     * @param sortDir Dirección (asc/desc), default: "desc"
     *
     * @return Respuesta paginada con DTOs de factura
     */
    @GetMapping("/paginated")
    public ResponseEntity<ApiResponse<PagedResponse<InvoiceDto>>> getAllInvoicesPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "date") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        PagedResponse<InvoiceDto> response = invoiceService.findAllPaginated(page, size, sortBy, sortDir);

        return ResponseEntity.ok(com.sena.sembrix.common.web.ApiResponse.<PagedResponse<InvoiceDto>>builder()
                .success(true)
                .message("Invoices retrieved successfully")
                .data(response)
                .build());
    }

    /**
     * GET /api/v1/invoices/by-customer/paginated
     *
     * Obtiene facturas por cliente con paginación
     *
     * @param customerId ID del cliente
     * @param page Número de página (0-indexed), default: 0
     * @param size Tamaño de página, default: 10
     * @param sortBy Campo para ordenar, default: "date"
     * @param sortDir Dirección (asc/desc), default: "desc"
     *
     * @return Respuesta paginada con facturas del cliente
     */
    @GetMapping("/by-customer/{customerId}/paginated")
    public ResponseEntity<ApiResponse<PagedResponse<InvoiceDto>>> getInvoicesByCustomerPaginated(
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "date") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        PagedResponse<InvoiceDto> response = invoiceService.findByCustomerIdPaginated(
                customerId, page, size, sortBy, sortDir);

        return ResponseEntity.ok(com.sena.sembrix.common.web.ApiResponse.<PagedResponse<InvoiceDto>>builder()
                .success(true)
                .message("Customer invoices retrieved successfully")
                .data(response)
                .build());
    }

    /**
     * GET /api/v1/invoices/by-producer/paginated
     *
     * Obtiene facturas por productor con paginación
     *
     * @param profileProducerId ID del productor
     * @param page Número de página (0-indexed), default: 0
     * @param size Tamaño de página, default: 10
     * @param sortBy Campo para ordenar, default: "date"
     * @param sortDir Dirección (asc/desc), default: "desc"
     *
     * @return Respuesta paginada con facturas del productor
     */
//    @GetMapping("/by-producer/{profileProducerId}/paginated")
//    public ResponseEntity<ApiResponse<PagedResponse<InvoiceDto>>> getInvoicesByProducerPaginated(
//            @PathVariable Long profileProducerId,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size,
//            @RequestParam(defaultValue = "date") String sortBy,
//            @RequestParam(defaultValue = "desc") String sortDir) {
//
//        PagedResponse<InvoiceDto> response = invoiceService.findByProfileProducerIdPaginated(
//                profileProducerId, page, size, sortBy, sortDir);
//
//        return ResponseEntity.ok(com.sena.sembrix.common.web.ApiResponse.<PagedResponse<InvoiceDto>>builder()
//                .success(true)
//                .message("Producer invoices retrieved successfully")
//                .data(response)
//                .build());
//    }

    /**
     * GET /api/v1/invoices/my-invoices/paginated
     *
     * Obtiene facturas del productor autenticado con paginación
     *
     * @param currentUser Usuario autenticado
     * @param page Número de página (0-indexed), default: 0
     * @param size Tamaño de página, default: 10
     * @param sortBy Campo para ordenar, default: "date"
     * @param sortDir Dirección (asc/desc), default: "desc"
     *
     * @return Respuesta paginada con facturas del usuario autenticado
     */
//    @GetMapping("/my-invoices/paginated")
//    public ResponseEntity<ApiResponse<PagedResponse<InvoiceDto>>> getMyInvoicesPaginated(
//            @AuthenticationPrincipal UserEntity currentUser,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size,
//            @RequestParam(defaultValue = "date") String sortBy,
//            @RequestParam(defaultValue = "desc") String sortDir) {
//
//        PagedResponse<InvoiceDto> response = invoiceService.findByProfileProducerIdPaginated(
//                currentUser.getId(), page, size, sortBy, sortDir);
//
//        return ResponseEntity.ok(ApiResponse.<PagedResponse<InvoiceDto>>builder()
//                .success(true)
//                .message("Your invoices retrieved successfully")
//                .data(response)
//                .build());
//    }
}
