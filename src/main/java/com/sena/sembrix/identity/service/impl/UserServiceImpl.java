package com.sena.sembrix.identity.service.impl;

import com.sena.sembrix.identity.ProfileProducer;
import com.sena.sembrix.identity.Role;
import com.sena.sembrix.identity.Status;
import com.sena.sembrix.identity.UserEntity;
import com.sena.sembrix.identity.dto.UserRequestDto;
import com.sena.sembrix.identity.dto.UserResponseDto;
import com.sena.sembrix.identity.mapper.UserMapper;
import com.sena.sembrix.identity.repository.ProfileProducerRepository;
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
    private final ProfileProducerRepository profileProducerRepository;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper,
                           BCryptPasswordEncoder passwordEncoder,
                           ProfileProducerRepository profileProducerRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.profileProducerRepository = profileProducerRepository;
    }

    @Override
    public UserResponseDto create(UserRequestDto dto) {
        // Validar que el correo no exista ya en el sistema
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("El correo electrónico ya está registrado: " + dto.getEmail());
        }
        UserEntity entity = userMapper.toEntity(dto);
        // Hashear contraseña
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        UserEntity saved = userRepository.save(entity);

        // Si el usuario es PRODUCER, crear automáticamente su perfil de productor
        if (saved.getRole() == Role.PRODUCER) {
            ProfileProducer profile = new ProfileProducer();
            profile.setUser(saved);
            profile.setFarmName(dto.getFarmName() != null ? dto.getFarmName() : "Granja de " + saved.getFirstName());
            profile.setRegion(dto.getRegion() != null ? dto.getRegion() : "");
            profile.setMunicipality(dto.getMunicipality() != null ? dto.getMunicipality() : "");
            profileProducerRepository.save(profile);
        }

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
    public com.sena.sembrix.common.web.PagedResponse<UserResponseDto> findAllPaginated(int page, int size, String sortBy, String sortDir) {
        org.springframework.data.domain.Sort sort = sortDir.equalsIgnoreCase(org.springframework.data.domain.Sort.Direction.ASC.name())
                ? org.springframework.data.domain.Sort.by(sortBy).ascending()
                : org.springframework.data.domain.Sort.by(sortBy).descending();

        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size, sort);
        org.springframework.data.domain.Page<UserEntity> usersPage = userRepository.findAll(pageable);

        org.springframework.data.domain.Page<UserResponseDto> dtoPage = usersPage.map(userMapper::toDto);
        
        return com.sena.sembrix.common.web.PagedResponse.from(dtoPage);
    }

    @Override
    public UserResponseDto update(Long id, UserRequestDto dto) {
        UserEntity u = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (dto.getEmail() != null && !dto.getEmail().equals(u.getEmail())) {
            if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
                throw new IllegalArgumentException("Email already in use: " + dto.getEmail());
            }
            u.setEmail(dto.getEmail());
        }
        
        if (dto.getFirstName() != null) u.setFirstName(dto.getFirstName());
        if (dto.getMiddleName() != null) u.setMiddleName(dto.getMiddleName());
        if (dto.getLastName() != null) u.setLastName(dto.getLastName());
        if (dto.getSecondLastName() != null) u.setSecondLastName(dto.getSecondLastName());
        
        if (dto.getRole() != null) {
            u.setRole(Role.valueOf(dto.getRole()));
        }
        
        if (dto.getStatus() != null) {
            u.setStatus(Status.valueOf(dto.getStatus()));
        }
        
        UserEntity saved = userRepository.save(u);
        return userMapper.toDto(saved);
    }

    @Override
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }
}

