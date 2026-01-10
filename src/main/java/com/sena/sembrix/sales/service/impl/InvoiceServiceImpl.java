package com.sena.sembrix.sales.service.impl;

import com.sena.sembrix.sales.Invoice;
import com.sena.sembrix.sales.dto.InvoiceDto;
import com.sena.sembrix.sales.mapper.InvoiceMapper;
import com.sena.sembrix.sales.repository.InvoiceRepository;
import com.sena.sembrix.sales.service.InvoiceService;
import com.sena.sembrix.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository repository;
    private final InvoiceMapper mapper;

    public InvoiceServiceImpl(InvoiceRepository repository, InvoiceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public InvoiceDto create(InvoiceDto dto) {
        Invoice entity = mapper.toEntity(dto);
        Invoice saved = repository.save(entity);
        return mapper.toDto(saved);
    }

    @Override
    public InvoiceDto findById(Long id) {
        Invoice inv = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));
        return mapper.toDto(inv);
    }

    @Override
    public List<InvoiceDto> findByCustomerId(Long customerId) {
        return repository.findByCustomerId(customerId).stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<InvoiceDto> findAll() {
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Invoice not found");
        }
        repository.deleteById(id);
    }
}

