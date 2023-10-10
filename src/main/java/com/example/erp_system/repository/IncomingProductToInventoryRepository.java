package com.example.erp_system.repository;

import com.example.erp_system.model.IncomingProductToInventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;

public interface IncomingProductToInventoryRepository extends JpaRepository<IncomingProductToInventory, Integer> {
    Optional<IncomingProductToInventory> findIncomingProductToInventoryByUpdatedAtBetween(Instant start, Instant end);

}
