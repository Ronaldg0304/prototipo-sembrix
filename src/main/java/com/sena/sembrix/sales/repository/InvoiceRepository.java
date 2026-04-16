package com.sena.sembrix.sales.repository;

import com.sena.sembrix.sales.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findByCustomerId(Long customerId);
    List<Invoice> findByCustomerProfileProducerId(Long profileProducerId);

    // Métodos paginados
    Page<Invoice> findByCustomerId(Long customerId, Pageable pageable);
    //Page<Invoice> findByProfileProducerId(Long profileProducerId, Pageable pageable);
}

