package com.sena.sembrix.market.service;

import com.sena.sembrix.market.dto.MarketDataDto;

import java.util.List;

public interface MarketDataService {
    MarketDataDto create(MarketDataDto dto);
    MarketDataDto findById(Long id);
    List<MarketDataDto> findBySourceApiName(String sourceApiName);
    List<MarketDataDto> findAll();
    void delete(Long id);
}

