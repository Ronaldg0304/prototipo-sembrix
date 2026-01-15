package com.sena.sembrix.sales.service;

import com.sena.sembrix.sales.dto.CustomerDto;
import org.checkerframework.checker.units.qual.C;

import java.util.List;

public interface CustomerService {
    CustomerDto create(CustomerDto dto);
    CustomerDto findById(Long id);
    CustomerDto findByEmail(String email);
    List<CustomerDto> findByProfileProducerId(Long profileProducerId);
    List<CustomerDto> findAll();
    CustomerDto update(Long id, CustomerDto dto);
    void delete(Long id);
}

