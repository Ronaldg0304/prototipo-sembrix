package com.sena.sembrix.sales.service;

import com.sena.sembrix.sales.dto.CustomerDto;

import java.util.List;

public interface CustomerService {
    CustomerDto create(CustomerDto dto);
    CustomerDto findById(Long id);
    CustomerDto findByEmail(String email);
    List<CustomerDto> findAll();
    void delete(Long id);
}

