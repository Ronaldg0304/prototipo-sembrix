package com.sena.sembrix.market.service;

import com.sena.sembrix.market.dto.MarketPriceDto;

import java.util.List;

public interface MarketPriceService {
    MarketPriceDto create(MarketPriceDto dto);
    MarketPriceDto findById(Long id);
    List<MarketPriceDto> findByProductId(Long productId);
    List<MarketPriceDto> findByRegion(String region);
    List<MarketPriceDto> findAll();
    void delete(Long id);
}

