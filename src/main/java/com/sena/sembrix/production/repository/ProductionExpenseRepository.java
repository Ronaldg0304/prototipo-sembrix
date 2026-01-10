package com.sena.sembrix.production.repository;

import com.sena.sembrix.production.ProductionExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductionExpenseRepository extends JpaRepository<ProductionExpense, Long> {
}

