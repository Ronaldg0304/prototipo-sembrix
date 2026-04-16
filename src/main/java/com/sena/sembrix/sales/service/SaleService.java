package com.sena.sembrix.sales.service;

import com.sena.sembrix.common.web.PagedResponse;
import com.sena.sembrix.sales.dto.SaleDto;

import java.util.List;

public interface SaleService {
    SaleDto create(SaleDto dto);
    SaleDto findById(Long id);
    List<SaleDto> findByProfileProducerId(Long profileProducerId);
    SaleDto findByInvoiceId(Long invoiceId);
    List<SaleDto> findAll();
    void delete(Long id);

    // Métodos paginados
    PagedResponse<SaleDto> findByProfileProducerIdPaginated(Long profileProducerId, int pageNo, int pageSize, String sortBy, String sortDir);
    PagedResponse<SaleDto> findAllPaginated(int pageNo, int pageSize, String sortBy, String sortDir);
}

