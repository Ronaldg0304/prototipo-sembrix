package com.sena.sembrix.market.repository;

import com.sena.sembrix.market.MarketPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MarketPriceRepository extends JpaRepository<MarketPrice, Long> {
    List<MarketPrice> findByProductId(Long productId);
    List<MarketPrice> findByRegion(String region);

    //@Query("SELECT m FROM MarketPrice m WHERE m.region = :region AND m.productId = :productId")
    Optional<MarketPrice> findByProductIdAndRegion(
            @Param("productId") Long productId,
            @Param("region") String region
    );

}

