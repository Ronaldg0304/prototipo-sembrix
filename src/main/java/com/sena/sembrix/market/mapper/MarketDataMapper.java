package com.sena.sembrix.market.mapper;

import com.sena.sembrix.market.MarketData;
import com.sena.sembrix.market.dto.MarketDataDto;
import org.springframework.stereotype.Component;

@Component
public class MarketDataMapper {

    public MarketData toEntity(MarketDataDto dto) {
        if (dto == null) return null;
        MarketData e = new MarketData();
        e.setId(dto.getId());
        e.setSourceApiName(dto.getSourceApiName());
        e.setExternalProductId(dto.getExternalProductId());
        e.setExternalPrice(dto.getExternalPrice());
        e.setCaptureDate(dto.getCaptureDate());
        return e;
    }

    public MarketDataDto toDto(MarketData entity) {
        if (entity == null) return null;
        MarketDataDto dto = new MarketDataDto();
        dto.setId(entity.getId());
        dto.setSourceApiName(entity.getSourceApiName());
        dto.setExternalProductId(entity.getExternalProductId());
        dto.setExternalPrice(entity.getExternalPrice());
        dto.setCaptureDate(entity.getCaptureDate());
        return dto;
    }
}

