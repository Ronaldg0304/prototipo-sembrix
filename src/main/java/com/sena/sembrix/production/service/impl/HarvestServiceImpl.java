package com.sena.sembrix.production.service.impl;

import com.sena.sembrix.common.web.PagedResponse;
import com.sena.sembrix.exception.ResourceNotFoundException;
import com.sena.sembrix.identity.ProfileProducer;
import com.sena.sembrix.identity.repository.ProfileProducerRepository;
import com.sena.sembrix.inventory.Inventory;
import com.sena.sembrix.inventory.Product;
import com.sena.sembrix.inventory.repository.InventoryRepository;
import com.sena.sembrix.inventory.repository.ProductRepository;
import com.sena.sembrix.production.*;
import com.sena.sembrix.production.dto.CloseHarvestDto;
import com.sena.sembrix.production.dto.CreateHarvestDto;
import com.sena.sembrix.production.dto.ExpenseDto;
import com.sena.sembrix.production.dto.HarvestDto;
import com.sena.sembrix.production.repository.HarvestRepository;
import com.sena.sembrix.production.repository.ProductionExpenseItemRepository;
import com.sena.sembrix.production.service.HarvestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class HarvestServiceImpl implements HarvestService {

    private final HarvestRepository harvestRepository;
    private final ProductionExpenseItemRepository expenseItemRepository;
    private final ProfileProducerRepository profileProducerRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    @Override
    public HarvestDto startHarvest(Long profileProducerId, CreateHarvestDto dto) {
        ProfileProducer producer = profileProducerRepository.findById(profileProducerId)
                .orElseThrow(() -> new ResourceNotFoundException("Producer not found"));

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Harvest harvest = Harvest.builder()
                .batchName(dto.getBatchName())
                .profileProducer(producer)
                .product(product)
                .status(HarvestStatus.OPEN)
                .startDate(dto.getStartDate() != null ? dto.getStartDate() : LocalDateTime.now())
                .totalExpenses(BigDecimal.ZERO)
                .build();

        Harvest saved = harvestRepository.save(harvest);
        return mapToDto(saved);
    }

    @Override
    public HarvestDto addExpense(Long harvestId, ExpenseDto dto) {
        Harvest harvest = harvestRepository.findById(harvestId)
                .orElseThrow(() -> new ResourceNotFoundException("Harvest not found"));

        if (harvest.getStatus() == HarvestStatus.CLOSED) {
            throw new IllegalStateException("Cannot add expense to a CLOSED harvest");
        }

        ProductionExpenseItem item = ProductionExpenseItem.builder()
                .description(dto.getDescription())
                .amount(dto.getAmount())
                .expenseDate(dto.getExpenseDate() != null ? dto.getExpenseDate() : LocalDateTime.now())
                .category(dto.getCategory())
                .harvest(harvest)
                .build();

        expenseItemRepository.save(item);

        // Update total expenses
        BigDecimal currentTotal = harvest.getTotalExpenses() != null ? harvest.getTotalExpenses() : BigDecimal.ZERO;
        harvest.setTotalExpenses(currentTotal.add(dto.getAmount()));
        
        Harvest saved = harvestRepository.save(harvest);
        return mapToDto(saved);
    }

    @Override
    public HarvestDto closeHarvest(Long harvestId, CloseHarvestDto dto) {
        Harvest harvest = harvestRepository.findById(harvestId)
                .orElseThrow(() -> new ResourceNotFoundException("Harvest not found"));

        if (harvest.getStatus() == HarvestStatus.CLOSED) {
            throw new IllegalStateException("Harvest is already CLOSED");
        }

        BigDecimal unitsProduced = BigDecimal.valueOf(dto.getUnitsProduced());
        BigDecimal salePrice = BigDecimal.valueOf(dto.getSalePrice());

        // Calculate Cost per Unit
        BigDecimal totalExpenses = harvest.getTotalExpenses();
        BigDecimal unitCost = BigDecimal.ZERO;
        
        if (unitsProduced.compareTo(BigDecimal.ZERO) > 0) {
            unitCost = totalExpenses.divide(unitsProduced, 2, RoundingMode.HALF_UP);
        }

        harvest.setUnitsProduced(unitsProduced);
        harvest.setUnitCost(unitCost);
        harvest.setStatus(HarvestStatus.CLOSED);
        harvest.setEndDate(LocalDateTime.now());
        
        Harvest savedHarvest = harvestRepository.save(harvest);

        // Update Inventory
        updateInventory(harvest.getProfileProducer(), harvest.getProduct(), unitsProduced, unitCost, salePrice);

        return mapToDto(savedHarvest);
    }

    private void updateInventory(ProfileProducer producer, Product product, BigDecimal unitsProduced, BigDecimal unitCost, BigDecimal salePrice) {
        Inventory inventory = inventoryRepository.findByProfileProducerIdAndProductId(producer.getId(), product.getId())
                .orElse(Inventory.builder()
                        .profileProducer(producer)
                        .product(product)
                        .currentStock(0.0)
                        .alertThreshold(10.0) // Default
                        .build());

        // Update Stock
        Double currentStock = inventory.getCurrentStock() != null ? inventory.getCurrentStock() : 0.0;
        inventory.setCurrentStock(currentStock + unitsProduced.doubleValue());
        
        // Update Unit Production Cost (Replacing for simplicity as per instructions)
        inventory.setUnitProductionCost(unitCost);
        
        // Update Sale Price
        inventory.setUnitPrice(salePrice.doubleValue());
        
        inventory.setLastUpdated(LocalDateTime.now());
        inventoryRepository.save(inventory);
    }

    @Override
    public List<HarvestDto> getHarvestsByProducer(Long profileProducerId) {
        return harvestRepository.findByProfileProducerId(profileProducerId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public HarvestDto getHarvestById(Long harvestId) {
         Harvest harvest = harvestRepository.findById(harvestId)
                .orElseThrow(() -> new ResourceNotFoundException("Harvest not found"));
         return mapToDto(harvest);
    }

    @Override
    public PagedResponse<HarvestDto> getHarvestsByProducerPaginated(Long profileProducerId, int pageNo, int pageSize, String sortBy, String sortDir) {
        // Validate sort direction
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir) ? Sort.Direction.DESC : Sort.Direction.ASC;

        // Create sort object
        Sort sort = Sort.by(direction, sortBy);

        // Create pageable
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, sort);

        // Fetch paginated data
        Page<Harvest> page = harvestRepository.findByProfileProducerId(profileProducerId, pageRequest);

        // Convert to DTO and build response
        List<HarvestDto> dtos = page.getContent().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        return PagedResponse.<HarvestDto>builder()
                .content(dtos)
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .first(page.isFirst())
                .numberOfElements(page.getNumberOfElements())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .build();
    }

    @Override
    public PagedResponse<HarvestDto> getHarvestsByProducerAndStatusPaginated(Long profileProducerId, HarvestStatus status, int pageNo, int pageSize, String sortBy, String sortDir) {
        // Validate sort direction
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir) ? Sort.Direction.DESC : Sort.Direction.ASC;

        // Create sort object
        Sort sort = Sort.by(direction, sortBy);

        // Create pageable
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, sort);

        // Fetch paginated data
        Page<Harvest> page = harvestRepository.findByProfileProducerIdAndStatus(profileProducerId, status, pageRequest);

        // Convert to DTO and build response
        List<HarvestDto> dtos = page.getContent().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        return PagedResponse.<HarvestDto>builder()
                .content(dtos)
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .first(page.isFirst())
                .numberOfElements(page.getNumberOfElements())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .build();
    }

    private HarvestDto mapToDto(Harvest harvest) {
        HarvestDto dto = new HarvestDto();
        dto.setId(harvest.getId());
        dto.setBatchName(harvest.getBatchName());
        dto.setStatus(harvest.getStatus());
        dto.setTotalExpenses(harvest.getTotalExpenses());
        dto.setUnitsProduced(harvest.getUnitsProduced());
        dto.setUnitCost(harvest.getUnitCost());
        dto.setStartDate(harvest.getStartDate());
        dto.setEndDate(harvest.getEndDate());
        dto.setProductId(harvest.getProduct().getId());
        dto.setProductName(harvest.getProduct().getName());
        return dto;
    }
}
