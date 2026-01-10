package com.sena.sembrix.sales.repository;

import com.sena.sembrix.sales.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
    List<Sale> findByProfileProducerId(Long profileProducerId);
    Optional<Sale> findByInvoiceId(Long invoiceId);
}

