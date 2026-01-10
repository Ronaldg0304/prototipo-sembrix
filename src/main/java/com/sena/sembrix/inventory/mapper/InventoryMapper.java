package com.sena.sembrix.inventory.mapper;

import com.sena.sembrix.identity.ProfileProducer;
import com.sena.sembrix.inventory.Inventory;
import com.sena.sembrix.inventory.Product;
import com.sena.sembrix.inventory.dto.InventoryDto;
import com.sena.sembrix.inventory.repository.ProductRepository;
import com.sena.sembrix.identity.repository.ProfileProducerRepository;
import org.springframework.stereotype.Component;

@Component
public class InventoryMapper {

    private final ProductRepository productRepository;
    private final ProfileProducerRepository profileProducerRepository;

    public InventoryMapper(ProductRepository productRepository, ProfileProducerRepository profileProducerRepository) {
        this.productRepository = productRepository;
        this.profileProducerRepository = profileProducerRepository;
    }

    public Inventory toEntity(InventoryDto dto) {
        if (dto == null) return null;
        Inventory e = new Inventory();
        e.setCurrentStock(dto.getCurrentStock());
        e.setUnitPrice(dto.getUnitPrice());
        e.setAlertThreshold(dto.getAlertThreshold());
        e.setLastUpdated(dto.getLastUpdated());

        if (dto.getProductId() != null) {
            Product p = productRepository.findById(dto.getProductId()).orElse(null);
            e.setProduct(p);
        }
        if (dto.getProfileProducerId() != null) {
            ProfileProducer pp = profileProducerRepository.findById(dto.getProfileProducerId()).orElse(null);
            e.setProfileProducer(pp);
        }
        return e;
    }

    public InventoryDto toDto(Inventory entity) {
        if (entity == null) return null;
        InventoryDto dto = new InventoryDto();
        dto.setId(entity.getId());
        dto.setCurrentStock(entity.getCurrentStock());
        dto.setUnitPrice(entity.getUnitPrice());
        dto.setAlertThreshold(entity.getAlertThreshold());
        dto.setLastUpdated(entity.getLastUpdated());
        if (entity.getProduct() != null) {
            dto.setProductId(entity.getProduct().getId());
        }
        if (entity.getProfileProducer() != null) {
            dto.setProfileProducerId(entity.getProfileProducer().getId());
        }
        return dto;
    }
}

