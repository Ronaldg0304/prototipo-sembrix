package com.sena.sembrix.production.repository;

import com.sena.sembrix.production.Harvest;
import com.sena.sembrix.production.HarvestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HarvestRepository extends JpaRepository<Harvest, Long> {
    List<Harvest> findByProfileProducerId(Long profileProducerId);
    List<Harvest> findByProfileProducerIdAndStatus(Long profileProducerId, HarvestStatus status);

    // Paginated methods
    Page<Harvest> findByProfileProducerId(Long profileProducerId, Pageable pageable);
    Page<Harvest> findByProfileProducerIdAndStatus(Long profileProducerId, HarvestStatus status, Pageable pageable);

    @Query("SELECT h FROM Harvest h WHERE h.profileProducer.id = :profileProducerId AND h.product.id = :productId ORDER BY h.createdAt DESC")
    List<Harvest> findMostRecentByProducerAndProduct(
            @Param("profileProducerId") Long profileProducerId,
            @Param("productId") Long productId
    );
}
