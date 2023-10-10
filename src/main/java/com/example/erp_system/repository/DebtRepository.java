package com.example.erp_system.repository;

import com.example.erp_system.model.Debt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface DebtRepository extends JpaRepository<Debt, Integer> {
    Optional<Debt> findDebtByUpdatedAtBetween(Instant start, Instant end);

}
