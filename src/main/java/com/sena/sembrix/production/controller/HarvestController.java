package com.sena.sembrix.production.controller;

import com.sena.sembrix.common.web.ApiResponse;
import com.sena.sembrix.common.web.PagedResponse;
import com.sena.sembrix.production.HarvestStatus;
import com.sena.sembrix.production.dto.CloseHarvestDto;
import com.sena.sembrix.production.dto.CreateHarvestDto;
import com.sena.sembrix.production.dto.ExpenseDto;
import com.sena.sembrix.production.dto.HarvestDto;
import com.sena.sembrix.production.service.HarvestService;
import com.sena.sembrix.identity.UserEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/harvests")
@RequiredArgsConstructor
public class HarvestController {

    private final HarvestService harvestService;

    @PostMapping
    public ResponseEntity<HarvestDto> createHarvest(
            @AuthenticationPrincipal UserEntity currentUser,
            @Valid @RequestBody CreateHarvestDto dto) {
        return ResponseEntity.ok(harvestService.startHarvest(currentUser.getId(), dto));
    }

    @PostMapping("/{id}/expenses")
    public ResponseEntity<HarvestDto> addExpense(
            @PathVariable Long id,
            @Valid @RequestBody ExpenseDto dto) {
        return ResponseEntity.ok(harvestService.addExpense(id, dto));
    }

    @PatchMapping("/{id}/close")
    public ResponseEntity<HarvestDto> closeHarvest(
            @PathVariable Long id,
            @RequestBody CloseHarvestDto dto) {
        return ResponseEntity.ok(harvestService.closeHarvest(id, dto));
    }

    @GetMapping
    public ResponseEntity<List<HarvestDto>> listHarvests(
            @AuthenticationPrincipal UserEntity currentUser) {
        return ResponseEntity.ok(harvestService.getHarvestsByProducer(currentUser.getId()));
    }

    /**
     * Get harvests with pagination support
     *
     * @param page Page number (0-indexed), default: 0
     * @param size Page size, default: 10
     * @param sortBy Field to sort by, default: "createdAt"
     * @param sortDir Sort direction (asc/desc), default: "desc"
     * @param status Optional: Filter by harvest status
     * @return Paginated harvest response
     */
    @GetMapping("/paginated")
    public ResponseEntity<ApiResponse<PagedResponse<HarvestDto>>> getHarvestsPaginated(
            @AuthenticationPrincipal UserEntity currentUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) HarvestStatus status) {

        PagedResponse<HarvestDto> response;

        if (status != null) {
            response = harvestService.getHarvestsByProducerAndStatusPaginated(
                    currentUser.getId(), status, page, size, sortBy, sortDir);
        } else {
            response = harvestService.getHarvestsByProducerPaginated(
                    currentUser.getId(), page, size, sortBy, sortDir);
        }

        return ResponseEntity.ok(com.sena.sembrix.common.web.ApiResponse.<PagedResponse<HarvestDto>>builder()
                .success(true)
                .message("Harvests retrieved successfully")
                .data(response)
                .build());
    }
}
