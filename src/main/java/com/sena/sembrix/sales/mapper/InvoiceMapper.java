package com.sena.sembrix.sales.mapper;

import com.sena.sembrix.sales.Customer;
import com.sena.sembrix.sales.Invoice;
import com.sena.sembrix.sales.InvoiceItem;
import com.sena.sembrix.sales.dto.InvoiceDto;
import com.sena.sembrix.sales.dto.InvoiceItemDto;
import com.sena.sembrix.sales.repository.CustomerRepository;
import com.sena.sembrix.sales.repository.InvoiceItemRepository;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class InvoiceMapper {

    private final CustomerRepository customerRepository;
    private final InvoiceItemRepository invoiceItemRepository;
    private final InvoiceItemMapper invoiceItemMapper;

    public InvoiceMapper(CustomerRepository customerRepository,
                        InvoiceItemRepository invoiceItemRepository,
                        InvoiceItemMapper invoiceItemMapper) {
        this.customerRepository = customerRepository;
        this.invoiceItemRepository = invoiceItemRepository;
        this.invoiceItemMapper = invoiceItemMapper;
    }

    public Invoice toEntity(InvoiceDto dto) {
        if (dto == null) return null;
        Invoice e = new Invoice();
        e.setTotalAmount(dto.getTotalAmount());
        e.setDate(dto.getDate());
        e.setStatus(dto.getStatus());

        if (dto.getCustomerId() != null) {
            Customer c = customerRepository.findById(dto.getCustomerId()).orElse(null);
            e.setCustomer(c);
        }
        return e;
    }

    public InvoiceDto toDto(Invoice entity) {
        if (entity == null) return null;
        InvoiceDto dto = new InvoiceDto();
        dto.setId(entity.getId());
        dto.setTotalAmount(entity.getTotalAmount());
        dto.setDate(entity.getDate());
        dto.setStatus(entity.getStatus());
        if (entity.getCustomer() != null) {
            dto.setCustomerId(entity.getCustomer().getId());
        }

        // Add invoice items
        if (entity.getId() != null) {
            dto.setItems(invoiceItemRepository.findByInvoiceId(entity.getId()).stream()
                    .map(invoiceItemMapper::toDto)
                    .collect(Collectors.toList()));
        }

        return dto;
    }
}



