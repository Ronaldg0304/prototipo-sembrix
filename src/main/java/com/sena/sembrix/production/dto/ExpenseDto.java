package com.sena.sembrix.production.dto;

import com.sena.sembrix.production.ExpenseCategory;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ExpenseDto {
    private String description;
    private BigDecimal amount;
    private LocalDateTime expenseDate;
    private ExpenseCategory category;
}
