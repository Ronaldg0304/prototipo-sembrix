package com.sena.sembrix.market.service.impl;

import com.sena.sembrix.market.MarketData;
import com.sena.sembrix.market.dto.MarketDataDto;
import com.sena.sembrix.market.mapper.MarketDataMapper;
import com.sena.sembrix.market.repository.MarketDataRepository;
import com.sena.sembrix.market.service.MarketDataService;
import com.sena.sembrix.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MarketDataServiceImpl implements MarketDataService {

    private final MarketDataRepository repository;
    private final MarketDataMapper mapper;

    public MarketDataServiceImpl(MarketDataRepository repository, MarketDataMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public MarketDataDto create(MarketDataDto dto) {
        MarketData entity = mapper.toEntity(dto);
        MarketData saved = repository.save(entity);
        return mapper.toDto(saved);
    }

    @Override
    public MarketDataDto findById(Long id) {
        MarketData md = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("MarketData not found"));
        return mapper.toDto(md);
    }

    @Override
    public List<MarketDataDto> findBySourceApiName(String sourceApiName) {
        return repository.findBySourceApiName(sourceApiName).stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<MarketDataDto> findAll() {
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("MarketData not found");
        }
        repository.deleteById(id);
    }
}

