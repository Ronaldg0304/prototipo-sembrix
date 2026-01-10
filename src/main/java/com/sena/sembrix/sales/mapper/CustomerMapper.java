package com.sena.sembrix.sales.mapper;

import com.sena.sembrix.sales.Customer;
import com.sena.sembrix.sales.dto.CustomerDto;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public Customer toEntity(CustomerDto dto) {
        if (dto == null) return null;
        Customer e = new Customer();
        e.setId(dto.getId());
        e.setFirstName(dto.getFirstName());
        e.setLastName(dto.getLastName());
        e.setPhone(dto.getPhone());
        e.setEmail(dto.getEmail());
        e.setAddress(dto.getAddress());
        return e;
    }

    public CustomerDto toDto(Customer entity) {
        if (entity == null) return null;
        CustomerDto dto = new CustomerDto();
        dto.setId(entity.getId());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setPhone(entity.getPhone());
        dto.setEmail(entity.getEmail());
        dto.setAddress(entity.getAddress());
        return dto;
    }
}

