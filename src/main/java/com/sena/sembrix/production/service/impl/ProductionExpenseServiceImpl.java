package com.sena.sembrix.production.service.impl;

import com.sena.sembrix.inventory.Inventory;
import com.sena.sembrix.inventory.repository.InventoryRepository;
import com.sena.sembrix.production.ExpenseCategory;
import com.sena.sembrix.production.ProductionExpense;
import com.sena.sembrix.production.ProductionExpenseItem;
import com.sena.sembrix.production.dto.ProductionExpenseDto;
import com.sena.sembrix.production.dto.ProductionExpenseItemDto;
import com.sena.sembrix.production.mapper.ProductionExpenseMapper;
import com.sena.sembrix.production.repository.ProductionExpenseItemRepository;
import com.sena.sembrix.production.repository.ProductionExpenseRepository;
import com.sena.sembrix.production.service.ProductionExpenseService;
import com.sena.sembrix.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductionExpenseServiceImpl implements ProductionExpenseService {

    private final ProductionExpenseRepository repository;
    private final ProductionExpenseItemRepository expenseItemRepository;
    private final InventoryRepository inventoryRepository;
    private final ProductionExpenseMapper mapper;

    public ProductionExpenseServiceImpl(ProductionExpenseRepository repository, 
                                      ProductionExpenseItemRepository expenseItemRepository, 
                                      InventoryRepository inventoryRepository,
                                      ProductionExpenseMapper mapper) {
        this.repository = repository;
        this.expenseItemRepository = expenseItemRepository;
        this.inventoryRepository = inventoryRepository;
        this.mapper = mapper;
    }

    @Override
    public ProductionExpenseDto create(ProductionExpenseDto dto) {
        // 1. Save Parent Expense Group
        ProductionExpense entity = mapper.toEntity(dto);
        ProductionExpense saved = repository.save(entity);

        // 2. Process Items if inventory is associated
        if (dto.getInventoryId() != null && dto.getItems() != null && !dto.getItems().isEmpty()) {
            Inventory inventory = inventoryRepository.findById(dto.getInventoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Inventory not found with ID: " + dto.getInventoryId()));

            for (ProductionExpenseItemDto itemDto : dto.getItems()) {
                ProductionExpenseItem itemEntity = ProductionExpenseItem.builder()
                        .description(itemDto.getDescription())
                        .amount(itemDto.getAmount())
                        .expenseDate(itemDto.getExpenseDate() != null ? itemDto.getExpenseDate() : LocalDateTime.now())
                        .category(ExpenseCategory.valueOf(itemDto.getCategory()))
                        .inventory(inventory)
                        .productionExpense(saved)
                        .build();
                expenseItemRepository.save(itemEntity);
            }
        }

        return mapper.toDto(saved);
    }

    @Override
    public ProductionExpenseDto findById(Long id) {
        ProductionExpense pe = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("ProductionExpense not found"));
        return mapper.toDto(pe);
    }

    @Override
    public List<ProductionExpenseDto> findByProfileProducerId(Long profileProducerId) {
        return repository.findByProfileProducerId(profileProducerId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductionExpenseDto> findAll() {
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("ProductionExpense not found");
        }
        repository.deleteById(id);
    }

    /**
     * Calcula el costo total acumulado de un inventario específico.
     */
    @Override
    public double calculateTotalCostByInventory(Long inventoryId) {
        return expenseItemRepository.findByInventoryId(inventoryId)
                .stream()
                .mapToDouble(ProductionExpenseItem::getAmount)
                .sum();
    }

    /**
     * Calcula el costo por unidad (Costo Total / Stock Actual).
     */
    @Override
    public double calculateUnitCost(Long inventoryId, Double currentStock) {
        if (currentStock <= 0) return 0.0;
        return calculateTotalCostByInventory(inventoryId) / currentStock;
    }
}

