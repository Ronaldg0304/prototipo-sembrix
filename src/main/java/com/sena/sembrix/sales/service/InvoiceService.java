package com.sena.sembrix.sales.service;

import com.sena.sembrix.common.web.PagedResponse;
import com.sena.sembrix.sales.dto.InvoiceDto;

import java.util.List;

public interface InvoiceService {
    InvoiceDto create(InvoiceDto dto);
    InvoiceDto findById(Long id);
    List<InvoiceDto> findByCustomerId(Long customerId);
    List<InvoiceDto> findByProfileProducerId(Long profileProducerId);
    List<InvoiceDto> findAll();
    void delete(Long id);

    // Métodos paginados
    PagedResponse<InvoiceDto> findByCustomerIdPaginated(Long customerId, int pageNo, int pageSize, String sortBy, String sortDir);
//    PagedResponse<InvoiceDto> findByProfileProducerIdPaginated(Long profileProducerId, int pageNo, int pageSize, String sortBy, String sortDir);
    PagedResponse<InvoiceDto> findAllPaginated(int pageNo, int pageSize, String sortBy, String sortDir);
}

