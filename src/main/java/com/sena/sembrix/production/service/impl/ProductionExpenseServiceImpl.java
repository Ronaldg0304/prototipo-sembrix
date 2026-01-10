package com.sena.sembrix.production.service.impl;

import com.sena.sembrix.production.ProductionExpense;
import com.sena.sembrix.production.dto.ProductionExpenseDto;
import com.sena.sembrix.production.mapper.ProductionExpenseMapper;
import com.sena.sembrix.production.repository.ProductionExpenseRepository;
import com.sena.sembrix.production.service.ProductionExpenseService;
import com.sena.sembrix.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductionExpenseServiceImpl implements ProductionExpenseService {

    private final ProductionExpenseRepository repository;
    private final ProductionExpenseMapper mapper;

    public ProductionExpenseServiceImpl(ProductionExpenseRepository repository, ProductionExpenseMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public ProductionExpenseDto create(ProductionExpenseDto dto) {
        ProductionExpense entity = mapper.toEntity(dto);
        ProductionExpense saved = repository.save(entity);
        return mapper.toDto(saved);
    }

    @Override
    public ProductionExpenseDto findById(Long id) {
        ProductionExpense pe = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("ProductionExpense not found"));
        return mapper.toDto(pe);
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
}

