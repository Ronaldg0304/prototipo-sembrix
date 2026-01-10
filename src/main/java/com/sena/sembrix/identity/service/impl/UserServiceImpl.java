package com.sena.sembrix.identity.service.impl;

import com.sena.sembrix.identity.UserEntity;
import com.sena.sembrix.identity.dto.UserRequestDto;
import com.sena.sembrix.identity.dto.UserResponseDto;
import com.sena.sembrix.identity.mapper.UserMapper;
import com.sena.sembrix.identity.repository.UserRepository;
import com.sena.sembrix.identity.service.UserService;
import com.sena.sembrix.exception.ResourceNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponseDto create(UserRequestDto dto) {
        UserEntity entity = userMapper.toEntity(dto);
        // set hashed password
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        UserEntity saved = userRepository.save(entity);
        return userMapper.toDto(saved);
    }

    @Override
    public UserResponseDto findById(Long id) {
        UserEntity u = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return userMapper.toDto(u);
    }

    @Override
    public List<UserResponseDto> findAll() {
        return userRepository.findAll().stream().map(userMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }
}

