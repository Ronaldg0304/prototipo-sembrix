package com.sena.sembrix.inventory.service.impl;

import com.sena.sembrix.common.web.PagedResponse;
import com.sena.sembrix.inventory.Inventory;
import com.sena.sembrix.inventory.dto.InventoryDto;
import com.sena.sembrix.inventory.mapper.InventoryMapper;
import com.sena.sembrix.inventory.repository.InventoryRepository;
import com.sena.sembrix.inventory.service.InventoryService;
import com.sena.sembrix.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    public Inventory findEntityById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Inventory not found"));
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
    public InventoryDto update(Long id, InventoryDto dto) {
        Inventory i = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Inventory not found"));
        i.setCurrentStock(dto.getCurrentStock());
        i.setUnitPrice(dto.getUnitPrice());
        i.setAlertThreshold(dto.getAlertThreshold());
        Inventory saved = repository.save(i);
        return mapper.toDto(saved);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Inventory not found");
        }
        repository.deleteById(id);
    }

    @Override
    public PagedResponse<InventoryDto> findByProfileProducerIdPaginated(Long profileProducerId, int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortBy);
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, sort);

        Page<Inventory> page = repository.findByProfileProducerId(profileProducerId, pageRequest);
        List<InventoryDto> dtos = page.getContent().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());

        return PagedResponse.<InventoryDto>builder()
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
    public PagedResponse<InventoryDto> findByProductIdPaginated(Long productId, int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortBy);
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, sort);

        Page<Inventory> page = repository.findByProductId(productId, pageRequest);
        List<InventoryDto> dtos = page.getContent().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());

        return PagedResponse.<InventoryDto>builder()
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
    public PagedResponse<InventoryDto> findAllPaginated(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortBy);
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, sort);

        Page<Inventory> page = repository.findAll(pageRequest);
        List<InventoryDto> dtos = page.getContent().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());

        return PagedResponse.<InventoryDto>builder()
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
}
