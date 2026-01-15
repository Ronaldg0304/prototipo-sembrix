package com.sena.sembrix.sales.mapper;

import com.sena.sembrix.sales.Customer;
import com.sena.sembrix.sales.dto.CustomerDto;
import com.sena.sembrix.identity.repository.ProfileProducerRepository;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    private final ProfileProducerRepository profileProducerRepository;

    public CustomerMapper(ProfileProducerRepository profileProducerRepository) {
        this.profileProducerRepository = profileProducerRepository;
    }

    public Customer toEntity(CustomerDto dto) {
        if (dto == null) return null;
        Customer e = new Customer();
        e.setId(dto.getId());
        e.setFirstName(dto.getFirstName());
        e.setLastName(dto.getLastName());
        e.setPhone(dto.getPhone());
        e.setEmail(dto.getEmail());
        e.setAddress(dto.getAddress());

        if (dto.getProfileProducerId() != null) {
            e.setProfileProducer(profileProducerRepository.findById(dto.getProfileProducerId()).orElse(null));
        }
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

        if (entity.getProfileProducer() != null) {
            dto.setProfileProducerId(entity.getProfileProducer().getId());
        }
        return dto;
    }
}

