package com.sena.sembrix.sales.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItemDto {
    private Long id;
    private Double quantity;
    private Double unitPrice;
    private Double subtotal;
    private Long invoiceId;
    private Long inventoryId;
}

