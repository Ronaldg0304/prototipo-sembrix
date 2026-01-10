package com.sena.sembrix.market.repository;

import com.sena.sembrix.market.MarketPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarketPriceRepository extends JpaRepository<MarketPrice, Long> {
    List<MarketPrice> findByProductId(Long productId);
    List<MarketPrice> findByRegion(String region);
}

