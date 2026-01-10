package com.sena.sembrix.market.mapper;

import com.sena.sembrix.inventory.Product;
import com.sena.sembrix.market.MarketPrice;
import com.sena.sembrix.market.dto.MarketPriceDto;
import com.sena.sembrix.inventory.repository.ProductRepository;
import org.springframework.stereotype.Component;

@Component
public class MarketPriceMapper {

    private final ProductRepository productRepository;

    public MarketPriceMapper(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public MarketPrice toEntity(MarketPriceDto dto) {
        if (dto == null) return null;
        MarketPrice e = new MarketPrice();
        e.setRegion(dto.getRegion());
        e.setAverageMarketPrice(dto.getAverageMarketPrice());
        e.setMaxPrice(dto.getMaxPrice());
        e.setMinPrice(dto.getMinPrice());
        e.setTrend(dto.getTrend());

        if (dto.getProductId() != null) {
            Product p = productRepository.findById(dto.getProductId()).orElse(null);
            e.setProduct(p);
        }
        return e;
    }

    public MarketPriceDto toDto(MarketPrice entity) {
        if (entity == null) return null;
        MarketPriceDto dto = new MarketPriceDto();
        dto.setId(entity.getId());
        dto.setRegion(entity.getRegion());
        dto.setAverageMarketPrice(entity.getAverageMarketPrice());
        dto.setMaxPrice(entity.getMaxPrice());
        dto.setMinPrice(entity.getMinPrice());
        dto.setTrend(entity.getTrend());
        if (entity.getProduct() != null) {
            dto.setProductId(entity.getProduct().getId());
        }
        return dto;
    }
}

