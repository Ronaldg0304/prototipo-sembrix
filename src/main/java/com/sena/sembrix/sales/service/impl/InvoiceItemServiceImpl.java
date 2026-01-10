package com.sena.sembrix.sales.service.impl;

import com.sena.sembrix.sales.InvoiceItem;
import com.sena.sembrix.sales.dto.InvoiceItemDto;
import com.sena.sembrix.sales.mapper.InvoiceItemMapper;
import com.sena.sembrix.sales.repository.InvoiceItemRepository;
import com.sena.sembrix.sales.service.InvoiceItemService;
import com.sena.sembrix.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class InvoiceItemServiceImpl implements InvoiceItemService {

    private final InvoiceItemRepository repository;
    private final InvoiceItemMapper mapper;

    public InvoiceItemServiceImpl(InvoiceItemRepository repository, InvoiceItemMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public InvoiceItemDto create(InvoiceItemDto dto) {
        InvoiceItem entity = mapper.toEntity(dto);
        InvoiceItem saved = repository.save(entity);
        return mapper.toDto(saved);
    }

    @Override
    public InvoiceItemDto findById(Long id) {
        InvoiceItem ii = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("InvoiceItem not found"));
        return mapper.toDto(ii);
    }

    @Override
    public List<InvoiceItemDto> findByInvoiceId(Long invoiceId) {
        return repository.findByInvoiceId(invoiceId).stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<InvoiceItemDto> findByInventoryId(Long inventoryId) {
        return repository.findByInventoryId(inventoryId).stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<InvoiceItemDto> findAll() {
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("InvoiceItem not found");
        }
        repository.deleteById(id);
    }
}

