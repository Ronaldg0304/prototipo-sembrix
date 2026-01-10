package com.sena.sembrix.inventory.service;

import com.sena.sembrix.inventory.dto.ProductDto;

import java.util.List;

public interface ProductService {
    ProductDto create(ProductDto dto);
    ProductDto findById(Long id);
    ProductDto findByName(String name);
    List<ProductDto> findAll();
    void delete(Long id);
}

