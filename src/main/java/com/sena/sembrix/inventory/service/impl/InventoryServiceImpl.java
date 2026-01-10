package com.sena.sembrix.inventory.service.impl;

import com.sena.sembrix.inventory.Inventory;
import com.sena.sembrix.inventory.dto.InventoryDto;
import com.sena.sembrix.inventory.mapper.InventoryMapper;
import com.sena.sembrix.inventory.repository.InventoryRepository;
import com.sena.sembrix.inventory.service.InventoryService;
import com.sena.sembrix.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository repository;
    private final InventoryMapper mapper;

    public InventoryServiceImpl(InventoryRepository repository, InventoryMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public InventoryDto create(InventoryDto dto) {
        Inventory entity = mapper.toEntity(dto);
        Inventory saved = repository.save(entity);
        return mapper.toDto(saved);
    }

    @Override
    public InventoryDto findById(Long id) {
        Inventory i = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Inventory not found"));
        return mapper.toDto(i);
    }

    @Override
    public List<InventoryDto> findByProfileProducerId(Long profileProducerId) {
        return repository.findByProfileProducerId(profileProducerId).stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<InventoryDto> findByProductId(Long productId) {
        return repository.findByProductId(productId).stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<InventoryDto> findAll() {
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Inventory not found");
        }
        repository.deleteById(id);
    }
}
