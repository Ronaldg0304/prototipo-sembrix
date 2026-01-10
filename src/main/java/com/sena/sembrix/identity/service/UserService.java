package com.sena.sembrix.identity.service;

import com.sena.sembrix.identity.dto.UserRequestDto;
import com.sena.sembrix.identity.dto.UserResponseDto;

import java.util.List;

public interface UserService {
    UserResponseDto create(UserRequestDto dto);
    UserResponseDto findById(Long id);
    List<UserResponseDto> findAll();
    void delete(Long id);
}

