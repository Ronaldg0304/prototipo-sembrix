package com.sena.sembrix.sales.mapper;

import com.sena.sembrix.identity.ProfileProducer;
import com.sena.sembrix.sales.Invoice;
import com.sena.sembrix.sales.Sale;
import com.sena.sembrix.sales.dto.SaleDto;
import com.sena.sembrix.sales.repository.InvoiceRepository;
import com.sena.sembrix.identity.repository.ProfileProducerRepository;
import org.springframework.stereotype.Component;

@Component
public class SalesMapper {

    private final InvoiceRepository invoiceRepository;
    private final ProfileProducerRepository profileProducerRepository;

    public SalesMapper(InvoiceRepository invoiceRepository, ProfileProducerRepository profileProducerRepository) {
        this.invoiceRepository = invoiceRepository;
        this.profileProducerRepository = profileProducerRepository;
    }

    public Sale toEntity(SaleDto dto) {
        if (dto == null) return null;
        Sale e = new Sale();
        e.setSalePrice(dto.getSalePrice());
        e.setTotalCost(dto.getTotalCost());
        e.setProfit(dto.getProfit());
        e.setDate(dto.getDate());

        if (dto.getInvoiceId() != null) {
            Invoice inv = invoiceRepository.findById(dto.getInvoiceId()).orElse(null);
            e.setInvoice(inv);
        }

        if (dto.getProfileProducerId() != null) {
            ProfileProducer pp = profileProducerRepository.findById(dto.getProfileProducerId()).orElse(null);
            e.setProfileProducer(pp);
        }
        return e;
    }

    public SaleDto toDto(Sale entity) {
        if (entity == null) return null;
        SaleDto dto = new SaleDto();
        dto.setId(entity.getId());
        dto.setSalePrice(entity.getSalePrice());
        dto.setTotalCost(entity.getTotalCost());
        dto.setProfit(entity.getProfit());
        dto.setDate(entity.getDate());
        if (entity.getInvoice() != null) {
            dto.setInvoiceId(entity.getInvoice().getId());
        }
        if (entity.getProfileProducer() != null) {
            dto.setProfileProducerId(entity.getProfileProducer().getId());
        }
        return dto;
    }
}

