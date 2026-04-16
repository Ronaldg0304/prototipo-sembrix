package com.sena.sembrix.market.application.service.impl;

import com.sena.sembrix.common.web.PagedResponse;
import com.sena.sembrix.inventory.Inventory;
import com.sena.sembrix.inventory.dto.InventoryDto;
import com.sena.sembrix.market.domain.MarketData;
import com.sena.sembrix.market.application.dto.MarketDataDto;
import com.sena.sembrix.market.application.mapper.MarketDataMapper;
import com.sena.sembrix.market.infrastructure.repository.MarketDataRepository;
import com.sena.sembrix.market.application.service.MarketDataService;
import com.sena.sembrix.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    @Override
    public PagedResponse<MarketDataDto> findAllPaginated(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortBy);
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, sort);

        Page<MarketData> page = repository.findAll(pageRequest);
        List<MarketDataDto> dtos = page.getContent().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        return PagedResponse.<MarketDataDto>builder()
                .content(dtos)
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .first(page.isFirst())
                .numberOfElements(page.getNumberOfElements())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .build();
    }
}

