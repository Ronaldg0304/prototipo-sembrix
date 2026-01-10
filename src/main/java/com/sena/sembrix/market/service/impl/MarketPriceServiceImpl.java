package com.sena.sembrix.market.service.impl;

import com.sena.sembrix.market.MarketPrice;
import com.sena.sembrix.market.dto.MarketPriceDto;
import com.sena.sembrix.market.mapper.MarketPriceMapper;
import com.sena.sembrix.market.repository.MarketPriceRepository;
import com.sena.sembrix.market.service.MarketPriceService;
import com.sena.sembrix.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("MarketPrice not found");
        }
        repository.deleteById(id);
    }
}

