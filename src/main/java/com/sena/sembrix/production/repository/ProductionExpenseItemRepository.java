package com.sena.sembrix.production.repository;

import com.sena.sembrix.production.ProductionExpenseItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductionExpenseItemRepository extends JpaRepository<ProductionExpenseItem, Long> {
    List<ProductionExpenseItem> findByProductionExpenseId(Long productionExpenseId);
    List<ProductionExpenseItem> findByInventoryId(Long inventoryId);
}

