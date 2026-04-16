package com.sena.sembrix.production;

import com.sena.sembrix.common.audit.Auditable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "production_expense_items")
public class ProductionExpenseItem extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private BigDecimal amount;
    private LocalDateTime expenseDate;

    @Enumerated(EnumType.STRING)
    private ExpenseCategory category;

    @ManyToOne(optional = false)
    @JoinColumn(name = "harvest_id", nullable = false)
    private Harvest harvest;
}
