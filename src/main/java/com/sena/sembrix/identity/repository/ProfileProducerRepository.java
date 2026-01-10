package com.sena.sembrix.identity.repository;

import com.sena.sembrix.identity.ProfileProducer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileProducerRepository extends JpaRepository<ProfileProducer, Long> {
    Optional<ProfileProducer> findByUserId(Long userId);
}

