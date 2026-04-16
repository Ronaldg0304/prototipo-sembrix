package com.sena.sembrix.market.application.service.impl;

import com.sena.sembrix.market.domain.MarketPrice;
import com.sena.sembrix.market.application.dto.MarketPriceDto;
import com.sena.sembrix.market.application.mapper.MarketPriceMapper;
import com.sena.sembrix.market.infrastructure.repository.MarketPriceRepository;
import com.sena.sembrix.market.application.service.MarketPriceService;
import com.sena.sembrix.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class MarketPriceServiceImpl implements MarketPriceService {

    private final MarketPriceRepository repository;
    private final MarketPriceMapper mapper;

    public MarketPriceServiceImpl(MarketPriceRepository repository, MarketPriceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public MarketPriceDto create(MarketPriceDto dto) {
        MarketPrice entity = mapper.toEntity(dto);
        MarketPrice saved = repository.save(entity);
        return mapper.toDto(saved);
    }

    @Override
    public MarketPriceDto findById(Long id) {
        MarketPrice mp = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("MarketPrice not found"));
        return mapper.toDto(mp);
    }

    @Override
    public List<MarketPriceDto> findByProductId(Long productId) {
        return repository.findByProductId(productId).stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<MarketPriceDto> findByRegion(String region) {
        return repository.findByRegion(region).stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<MarketPriceDto> findAll() {
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public MarketPriceDto update(Long id, MarketPriceDto dto) {
        MarketPrice entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MarketPrice not found"));
        
        if (dto.getAverageMarketPrice() != null) entity.setAverageMarketPrice(dto.getAverageMarketPrice());
        if (dto.getMaxPrice() != null) entity.setMaxPrice(dto.getMaxPrice());
        if (dto.getMinPrice() != null) entity.setMinPrice(dto.getMinPrice());
        if (dto.getTrend() != null) entity.setTrend(dto.getTrend());
        if (dto.getRegion() != null) entity.setRegion(dto.getRegion());
        
        MarketPrice saved = repository.save(entity);
        return mapper.toDto(saved);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("MarketPrice not found");
        }
        repository.deleteById(id);
    }

    /**
     * Obtiene el precio promedio del mercado para un producto en una región.
     */
    public Optional<MarketPriceDto> getRegionalPrice(Long productId, String region) {
        Optional<MarketPrice> optionalMarketPrice = repository.findByProductIdAndRegionIgnoreCase(productId, region);
        if (optionalMarketPrice.isEmpty()) {
            throw new ResourceNotFoundException("Market price not found with id: " + productId + " and region: " + region);
        }
        return optionalMarketPrice.map(mapper::toDto);
    }
}

