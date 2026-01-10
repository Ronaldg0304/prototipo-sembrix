package com.sena.sembrix.inventory;

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
@Table(name = "inventory")
public class Inventory extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double currentStock;
    private Double unitPrice;
    private Double alertThreshold;
    private LocalDateTime lastUpdated;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(optional = false)
    @JoinColumn(name = "profile_producer_id", nullable = false)
    private ProfileProducer profileProducer;
}
