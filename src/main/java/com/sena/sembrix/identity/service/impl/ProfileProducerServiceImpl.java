package com.sena.sembrix.identity.service.impl;

import com.sena.sembrix.identity.ProfileProducer;
import com.sena.sembrix.identity.dto.ProfileProducerDto;
import com.sena.sembrix.identity.mapper.ProfileProducerMapper;
import com.sena.sembrix.identity.repository.ProfileProducerRepository;
import com.sena.sembrix.identity.service.ProfileProducerService;
import com.sena.sembrix.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProfileProducerServiceImpl implements ProfileProducerService {

    private final ProfileProducerRepository repository;
    private final ProfileProducerMapper mapper;

    public ProfileProducerServiceImpl(ProfileProducerRepository repository, ProfileProducerMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public ProfileProducerDto create(ProfileProducerDto dto) {
        ProfileProducer entity = mapper.toEntity(dto);
        ProfileProducer saved = repository.save(entity);
        return mapper.toDto(saved);
    }

    @Override
    public ProfileProducerDto findById(Long id) {
        ProfileProducer p = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("ProfileProducer not found"));
        return mapper.toDto(p);
    }

    @Override
    public ProfileProducerDto findByUserId(Long userId) {
        ProfileProducer p = repository.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("ProfileProducer not found for user"));
        return mapper.toDto(p);
    }

    @Override
    public List<ProfileProducerDto> findAll() {
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public ProfileProducerDto update(Long id, ProfileProducerDto dto) {
        ProfileProducer p = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("ProfileProducer not found"));
        p.setFarmName(dto.getFarmName());
        p.setFarmSize(dto.getFarmSize());
        p.setRegion(dto.getRegion());
        p.setMunicipality(dto.getMunicipality());
        p.setAssociationName(dto.getAssociationName());
        ProfileProducer updated = repository.save(p);
        return mapper.toDto(updated);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("ProfileProducer not found");
        }
        repository.deleteById(id);
    }
}
