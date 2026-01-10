package com.sena.sembrix.sales.mapper;

import com.sena.sembrix.inventory.Inventory;
import com.sena.sembrix.sales.Invoice;
import com.sena.sembrix.sales.InvoiceItem;
import com.sena.sembrix.sales.dto.InvoiceItemDto;
import com.sena.sembrix.sales.repository.InvoiceRepository;
import com.sena.sembrix.inventory.repository.InventoryRepository;
import org.springframework.stereotype.Component;

@Component
public class InvoiceItemMapper {

    private final InvoiceRepository invoiceRepository;
    private final InventoryRepository inventoryRepository;

    public InvoiceItemMapper(InvoiceRepository invoiceRepository, InventoryRepository inventoryRepository) {
        this.invoiceRepository = invoiceRepository;
        this.inventoryRepository = inventoryRepository;
    }

    public InvoiceItem toEntity(InvoiceItemDto dto) {
        if (dto == null) return null;
        InvoiceItem e = new InvoiceItem();
        e.setQuantity(dto.getQuantity());
        e.setUnitPrice(dto.getUnitPrice());
        e.setSubtotal(dto.getSubtotal());

        if (dto.getInvoiceId() != null) {
            Invoice inv = invoiceRepository.findById(dto.getInvoiceId()).orElse(null);
            e.setInvoice(inv);
        }
        if (dto.getInventoryId() != null) {
            Inventory invent = inventoryRepository.findById(dto.getInventoryId()).orElse(null);
            e.setInventory(invent);
        }
        return e;
    }

    public InvoiceItemDto toDto(InvoiceItem entity) {
        if (entity == null) return null;
        InvoiceItemDto dto = new InvoiceItemDto();
        dto.setId(entity.getId());
        dto.setQuantity(entity.getQuantity());
        dto.setUnitPrice(entity.getUnitPrice());
        dto.setSubtotal(entity.getSubtotal());
        if (entity.getInvoice() != null) {
            dto.setInvoiceId(entity.getInvoice().getId());
        }
        if (entity.getInventory() != null) {
            dto.setInventoryId(entity.getInventory().getId());
        }
        return dto;
    }
}

