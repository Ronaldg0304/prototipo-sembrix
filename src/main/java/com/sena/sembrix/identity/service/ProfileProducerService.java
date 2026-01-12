package com.sena.sembrix.identity.service;

import com.sena.sembrix.identity.dto.ProfileProducerDto;
import com.sena.sembrix.identity.dto.UserRequestDto;
import com.sena.sembrix.identity.dto.UserResponseDto;

import java.util.List;

public interface ProfileProducerService {
    ProfileProducerDto create(ProfileProducerDto dto);
    ProfileProducerDto findById(Long id);
    ProfileProducerDto findByUserId(Long userId);
    List<ProfileProducerDto> findAll();
    ProfileProducerDto update(Long id, ProfileProducerDto dto);
    void delete(Long id);
}

