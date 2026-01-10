package com.sena.sembrix.identity.mapper;

import com.sena.sembrix.identity.ProfileProducer;
import com.sena.sembrix.identity.UserEntity;
import com.sena.sembrix.identity.dto.ProfileProducerDto;
import com.sena.sembrix.identity.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class ProfileProducerMapper {

    private final UserRepository userRepository;

    public ProfileProducerMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ProfileProducer toEntity(ProfileProducerDto dto) {
        if (dto == null) return null;
        ProfileProducer e = new ProfileProducer();
        e.setFarmName(dto.getFarmName());
        e.setRegion(dto.getRegion());
        e.setMunicipality(dto.getMunicipality());
        e.setFarmSize(dto.getFarmSize());
        e.setAssociationName(dto.getAssociationName());

        if (dto.getUserId() != null) {
            UserEntity user = userRepository.findById(dto.getUserId()).orElse(null);
            e.setUser(user);
        }
        return e;
    }

    public ProfileProducerDto toDto(ProfileProducer entity) {
        if (entity == null) return null;
        ProfileProducerDto dto = new ProfileProducerDto();
        dto.setId(entity.getId());
        dto.setFarmName(entity.getFarmName());
        dto.setRegion(entity.getRegion());
        dto.setMunicipality(entity.getMunicipality());
        dto.setFarmSize(entity.getFarmSize());
        dto.setAssociationName(entity.getAssociationName());
        if (entity.getUser() != null) {
            dto.setUserId(entity.getUser().getId());
        }
        return dto;
    }
}
