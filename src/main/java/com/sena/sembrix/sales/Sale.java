package com.sena.sembrix.sales;

import com.sena.sembrix.common.audit.Auditable;
import com.sena.sembrix.identity.ProfileProducer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "sales")
public class Sale extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double salePrice;
    private Double totalCost;
    private Double profit;
    private LocalDateTime date;

    @OneToOne(optional = false)
    @JoinColumn(name = "invoice_id", nullable = false, unique = true)
    private Invoice invoice;

    @ManyToOne(optional = false)
    @JoinColumn(name = "profile_producer_id", nullable = false)
    private ProfileProducer profileProducer;
}

