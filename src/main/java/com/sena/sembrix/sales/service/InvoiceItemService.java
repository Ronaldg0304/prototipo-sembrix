package com.sena.sembrix.sales.service;

import com.sena.sembrix.sales.dto.InvoiceItemDto;

import java.util.List;

public interface InvoiceItemService {
    InvoiceItemDto create(InvoiceItemDto dto);
    InvoiceItemDto findById(Long id);
    List<InvoiceItemDto> findByInvoiceId(Long invoiceId);
    List<InvoiceItemDto> findByInventoryId(Long inventoryId);
    List<InvoiceItemDto> findAll();
    void delete(Long id);
}

