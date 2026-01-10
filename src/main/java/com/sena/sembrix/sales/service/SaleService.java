package com.sena.sembrix.sales.service;

import com.sena.sembrix.sales.dto.SaleDto;

import java.util.List;

public interface SaleService {
    SaleDto create(SaleDto dto);
    SaleDto findById(Long id);
    List<SaleDto> findByProfileProducerId(Long profileProducerId);
    SaleDto findByInvoiceId(Long invoiceId);
    List<SaleDto> findAll();
    void delete(Long id);
}

