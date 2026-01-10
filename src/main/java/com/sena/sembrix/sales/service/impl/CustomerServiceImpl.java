package com.sena.sembrix.sales.service.impl;

import com.sena.sembrix.sales.Customer;
import com.sena.sembrix.sales.dto.CustomerDto;
import com.sena.sembrix.sales.mapper.CustomerMapper;
import com.sena.sembrix.sales.repository.CustomerRepository;
import com.sena.sembrix.sales.service.CustomerService;
import com.sena.sembrix.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;
    private final CustomerMapper mapper;

    public CustomerServiceImpl(CustomerRepository repository, CustomerMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public CustomerDto create(CustomerDto dto) {
        Customer entity = mapper.toEntity(dto);
        Customer saved = repository.save(entity);
        return mapper.toDto(saved);
    }

    @Override
    public CustomerDto findById(Long id) {
        Customer c = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        return mapper.toDto(c);
    }

    @Override
    public CustomerDto findByEmail(String email) {
        Customer c = repository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        return mapper.toDto(c);
    }

    @Override
    public List<CustomerDto> findAll() {
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Customer not found");
        }
        repository.deleteById(id);
    }
}

