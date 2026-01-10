package com.sena.sembrix.sales.service;

import com.sena.sembrix.sales.dto.InvoiceDto;

import java.util.List;

public interface InvoiceService {
    InvoiceDto create(InvoiceDto dto);
    InvoiceDto findById(Long id);
    List<InvoiceDto> findByCustomerId(Long customerId);
    List<InvoiceDto> findAll();
    void delete(Long id);
}

