package com.sena.sembrix.sales;

import com.sena.sembrix.common.audit.Auditable;
import com.sena.sembrix.identity.ProfileProducer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "customers")
public class Customer extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String phone;

    @Column(unique = true)
    private String email;

    private String address;

    @ManyToOne(optional = false)
    @JoinColumn(name = "profile_producer_id", nullable = false)
    private ProfileProducer profileProducer;
}

