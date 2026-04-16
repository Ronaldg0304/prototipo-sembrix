package com.sena.sembrix.market.application.service;

import com.sena.sembrix.common.web.PagedResponse;
import com.sena.sembrix.market.application.dto.MarketDataDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MarketDataService {
    MarketDataDto create(MarketDataDto dto);
    MarketDataDto findById(Long id);
    List<MarketDataDto> findBySourceApiName(String sourceApiName);
    List<MarketDataDto> findAll();
    void delete(Long id);

    // Métodos paginados
    PagedResponse<MarketDataDto> findAllPaginated(int pageNo, int pageSize, String sortBy, String sortDir);
}

