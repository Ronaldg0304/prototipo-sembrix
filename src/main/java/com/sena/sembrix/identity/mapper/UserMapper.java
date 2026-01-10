package com.sena.sembrix.identity.mapper;

import com.sena.sembrix.identity.Role;
import com.sena.sembrix.identity.Status;
import com.sena.sembrix.identity.UserEntity;
import com.sena.sembrix.identity.dto.UserRequestDto;
import com.sena.sembrix.identity.dto.UserResponseDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserEntity toEntity(UserRequestDto dto) {
        if (dto == null) return null;
        UserEntity e = new UserEntity();
        e.setFirstName(dto.getFirstName());
        e.setMiddleName(dto.getMiddleName());
        e.setLastName(dto.getLastName());
        e.setSecondLastName(dto.getSecondLastName());
        e.setEmail(dto.getEmail());
        // password is set in service after hashing
        if (dto.getRole() != null) {
            try { e.setRole(Role.valueOf(dto.getRole())); } catch (Exception ex) { e.setRole(Role.PRODUCER); }
        } else {
            e.setRole(Role.PRODUCER);
        }
        if (dto.getStatus() != null) {
            try { e.setStatus(Status.valueOf(dto.getStatus())); } catch (Exception ex) { e.setStatus(Status.ACTIVE); }
        } else {
            e.setStatus(Status.ACTIVE);
        }
        return e;
    }

    public UserResponseDto toDto(UserEntity entity) {
        if (entity == null) return null;
        UserResponseDto dto = new UserResponseDto();
        dto.setId(entity.getId());
        dto.setFirstName(entity.getFirstName());
        dto.setMiddleName(entity.getMiddleName());
        dto.setLastName(entity.getLastName());
        dto.setSecondLastName(entity.getSecondLastName());
        dto.setEmail(entity.getEmail());
        dto.setRole(entity.getRole() != null ? entity.getRole().name() : null);
        dto.setStatus(entity.getStatus() != null ? entity.getStatus().name() : null);
        return dto;
    }
}
