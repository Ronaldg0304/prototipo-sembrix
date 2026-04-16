package com.sena.sembrix.production.dto;

import com.sena.sembrix.production.HarvestStatus;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class HarvestDto {
    private Long id;
    private String batchName;
    private HarvestStatus status;
    private BigDecimal totalExpenses;
    private BigDecimal unitsProduced;
    private BigDecimal unitCost;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long productId;
    private String productName;
}
