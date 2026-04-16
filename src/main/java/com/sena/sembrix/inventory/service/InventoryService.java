package com.sena.sembrix.inventory.service;

import com.sena.sembrix.common.web.PagedResponse;
import com.sena.sembrix.inventory.Inventory;
import com.sena.sembrix.inventory.dto.InventoryDto;

import java.util.List;

public interface InventoryService {
    InventoryDto create(InventoryDto dto);
    InventoryDto findById(Long id);
    Inventory findEntityById(Long id);
    List<InventoryDto> findByProfileProducerId(Long profileProducerId);
    List<InventoryDto> findByProductId(Long productId);
    List<InventoryDto> findAll();
    InventoryDto update(Long id, InventoryDto dto);
    void delete(Long id);

    // Métodos paginados
    PagedResponse<InventoryDto> findByProfileProducerIdPaginated(Long profileProducerId, int pageNo, int pageSize, String sortBy, String sortDir);
    PagedResponse<InventoryDto> findByProductIdPaginated(Long productId, int pageNo, int pageSize, String sortBy, String sortDir);
    PagedResponse<InventoryDto> findAllPaginated(int pageNo, int pageSize, String sortBy, String sortDir);
}

