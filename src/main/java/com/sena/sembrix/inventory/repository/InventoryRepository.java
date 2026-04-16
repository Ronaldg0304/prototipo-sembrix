package com.sena.sembrix.inventory.repository;

import com.sena.sembrix.inventory.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    List<Inventory> findByProfileProducerId(Long profileProducerId);
    List<Inventory> findByProductId(Long productId);
    Optional<Inventory> findByProfileProducerIdAndProductId(Long profileProducerId, Long productId);

    // Paginated methods
    Page<Inventory> findByProfileProducerId(Long profileProducerId, Pageable pageable);
    Page<Inventory> findByProductId(Long productId, Pageable pageable);
}

