package com.sena.sembrix.identity.service;

import com.sena.sembrix.identity.dto.UserRequestDto;
import com.sena.sembrix.identity.dto.UserResponseDto;

import com.sena.sembrix.common.web.PagedResponse;

import java.util.List;

public interface UserService {
    UserResponseDto create(UserRequestDto dto);
    UserResponseDto findById(Long id);
    List<UserResponseDto> findAll();
    PagedResponse<UserResponseDto> findAllPaginated(int page, int size, String sortBy, String sortDir);
    UserResponseDto update(Long id, UserRequestDto dto);
    void delete(Long id);
}

