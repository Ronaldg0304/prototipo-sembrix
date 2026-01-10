package com.sena.sembrix.production.service.impl;

import com.sena.sembrix.production.ProductionExpenseItem;
import com.sena.sembrix.production.dto.ProductionExpenseItemDto;
import com.sena.sembrix.production.mapper.ProductionExpenseItemMapper;
import com.sena.sembrix.production.repository.ProductionExpenseItemRepository;
import com.sena.sembrix.production.service.ProductionExpenseItemService;
import com.sena.sembrix.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductionExpenseItemServiceImpl implements ProductionExpenseItemService {

    private final ProductionExpenseItemRepository repository;
    private final ProductionExpenseItemMapper mapper;

    public ProductionExpenseItemServiceImpl(ProductionExpenseItemRepository repository, ProductionExpenseItemMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public ProductionExpenseItemDto create(ProductionExpenseItemDto dto) {
        ProductionExpenseItem entity = mapper.toEntity(dto);
        ProductionExpenseItem saved = repository.save(entity);
        return mapper.toDto(saved);
    }

    @Override
    public ProductionExpenseItemDto findById(Long id) {
        ProductionExpenseItem pei = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("ProductionExpenseItem not found"));
        return mapper.toDto(pei);
    }

    @Override
    public List<ProductionExpenseItemDto> findByProductionExpenseId(Long productionExpenseId) {
        return repository.findByProductionExpenseId(productionExpenseId).stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<ProductionExpenseItemDto> findByInventoryId(Long inventoryId) {
        return repository.findByInventoryId(inventoryId).stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<ProductionExpenseItemDto> findAll() {
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("ProductionExpenseItem not found");
        }
        repository.deleteById(id);
    }
}

