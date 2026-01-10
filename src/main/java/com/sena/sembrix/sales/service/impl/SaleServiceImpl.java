package com.sena.sembrix.sales.service.impl;

import com.sena.sembrix.sales.Sale;
import com.sena.sembrix.sales.dto.SaleDto;
import com.sena.sembrix.sales.mapper.SalesMapper;
import com.sena.sembrix.sales.repository.SaleRepository;
import com.sena.sembrix.sales.service.SaleService;
import com.sena.sembrix.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SaleServiceImpl implements SaleService {

    private final SaleRepository repository;
    private final SalesMapper mapper;

    public SaleServiceImpl(SaleRepository repository, SalesMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public SaleDto create(SaleDto dto) {
        Sale entity = mapper.toEntity(dto);
        Sale saved = repository.save(entity);
        return mapper.toDto(saved);
    }

    @Override
    public SaleDto findById(Long id) {
        Sale s = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Sale not found"));
        return mapper.toDto(s);
    }

    @Override
    public List<SaleDto> findByProfileProducerId(Long profileProducerId) {
        return repository.findByProfileProducerId(profileProducerId).stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public SaleDto findByInvoiceId(Long invoiceId) {
        Sale s = repository.findByInvoiceId(invoiceId).orElseThrow(() -> new ResourceNotFoundException("Sale not found"));
        return mapper.toDto(s);
    }

    @Override
    public List<SaleDto> findAll() {
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Sale not found");
        }
        repository.deleteById(id);
    }
}

