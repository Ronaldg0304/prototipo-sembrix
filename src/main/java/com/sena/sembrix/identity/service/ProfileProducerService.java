package com.sena.sembrix.identity.service;

import com.sena.sembrix.identity.dto.ProfileProducerDto;

import java.util.List;

public interface ProfileProducerService {
    ProfileProducerDto create(ProfileProducerDto dto);
    ProfileProducerDto findById(Long id);
    ProfileProducerDto findByUserId(Long userId);
    List<ProfileProducerDto> findAll();
    void delete(Long id);
}

