package com.sena.sembrix.market.service;

import aj.org.objectweb.asm.commons.Remapper;
import com.sena.sembrix.market.dto.MarketPriceDto;

import java.util.List;
import java.util.Optional;

public interface MarketPriceService {
    MarketPriceDto create(MarketPriceDto dto);
    MarketPriceDto findById(Long id);
    List<MarketPriceDto> findByProductId(Long productId);
    List<MarketPriceDto> findByRegion(String region);
    List<MarketPriceDto> findAll();
    MarketPriceDto update(Long id, MarketPriceDto dto);
    void delete(Long id);
    Optional<MarketPriceDto> getRegionalPrice(Long id, String region);
}

