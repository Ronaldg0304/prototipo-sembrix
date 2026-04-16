package com.sena.sembrix.market.infrastructure.repository;

import com.sena.sembrix.market.domain.MarketData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarketDataRepository extends JpaRepository<MarketData, Long> {
    List<MarketData> findBySourceApiName(String sourceApiName);

    @Query("SELECT m FROM MarketData m WHERE LOWER(m.region) = LOWER(:region) AND m.externalProductId = :externalProductId ORDER BY m.captureDate DESC")
    List<MarketData> findLatestByRegionAndExternalId(
            @Param("region") String region,
            @Param("externalProductId") String externalProductId
    );
    
    @Query("SELECT m FROM MarketData m WHERE LOWER(m.region) = LOWER(:region) ORDER BY m.captureDate DESC")
    List<MarketData> findLatestByRegion(@Param("region") String region);

}

