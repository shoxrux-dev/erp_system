package com.example.erp_system.repository;

import com.example.erp_system.model.Sales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface SalesRepository extends JpaRepository<Sales, Integer> {
    Optional<Sales> findByUpdatedAtBetween(Instant start, Instant end);
}
