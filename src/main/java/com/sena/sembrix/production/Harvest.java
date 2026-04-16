package com.sena.sembrix.production;

import com.sena.sembrix.common.audit.Auditable;
import com.sena.sembrix.identity.ProfileProducer;
import com.sena.sembrix.inventory.Product;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "harvests")
public class Harvest extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String batchName;

    @Enumerated(EnumType.STRING)
    private HarvestStatus status;

    private BigDecimal totalExpenses = BigDecimal.ZERO;

    private BigDecimal unitsProduced;

    private BigDecimal unitCost;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "profile_producer_id", nullable = false)
    private ProfileProducer profileProducer;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @OneToMany(mappedBy = "harvest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductionExpenseItem> expenses;
}
