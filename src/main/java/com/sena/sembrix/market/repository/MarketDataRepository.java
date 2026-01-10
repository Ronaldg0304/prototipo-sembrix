package com.sena.sembrix.market.repository;

import com.sena.sembrix.market.MarketData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarketDataRepository extends JpaRepository<MarketData, Long> {
    List<MarketData> findBySourceApiName(String sourceApiName);
}

