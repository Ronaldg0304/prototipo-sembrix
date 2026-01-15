package com.sena.sembrix.production.repository;

import com.sena.sembrix.production.ProductionExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductionExpenseRepository extends JpaRepository<ProductionExpense, Long> {
    
    @Query("SELECT DISTINCT pe FROM ProductionExpense pe JOIN ProductionExpenseItem pei ON pei.productionExpense = pe " +
           "WHERE pei.inventory.profileProducer.id = :profileProducerId")
    List<ProductionExpense> findByProfileProducerId(@Param("profileProducerId") Long profileProducerId);
}

