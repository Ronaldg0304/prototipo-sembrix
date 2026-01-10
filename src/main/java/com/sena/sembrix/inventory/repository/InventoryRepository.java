package com.sena.sembrix.inventory.repository;

import com.sena.sembrix.inventory.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    List<Inventory> findByProfileProducerId(Long profileProducerId);
    List<Inventory> findByProductId(Long productId);
}

