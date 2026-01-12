package com.sena.sembrix.inventory.service.impl;

import com.sena.sembrix.inventory.Product;
import com.sena.sembrix.inventory.dto.ProductDto;
import com.sena.sembrix.inventory.mapper.ProductMapper;
import com.sena.sembrix.inventory.repository.ProductRepository;
import com.sena.sembrix.inventory.service.ProductService;
import com.sena.sembrix.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper;

    public ProductServiceImpl(ProductRepository repository, ProductMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public ProductDto create(ProductDto dto) {
        Product entity = mapper.toEntity(dto);
        Product saved = repository.save(entity);
        return mapper.toDto(saved);
    }

    @Override
    public ProductDto findById(Long id) {
        Product p = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        return mapper.toDto(p);
    }

    @Override
    public ProductDto findByName(String name) {
        Product p = repository.findByName(name).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        return mapper.toDto(p);
    }

    @Override
    public List<ProductDto> findAll() {
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public ProductDto update(Long id, ProductDto dto) {
        Product p = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        p.setName(dto.getName());
        p.setDescription(dto.getDescription());
        p.setCategory(dto.getCategory());
        p.setUnitOfMeasure(dto.getUnitOfMeasure());
        Product saved = repository.save(p);
        return mapper.toDto(saved);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found");
        }
        repository.deleteById(id);
    }
}
