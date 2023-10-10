package com.example.erp_system.repository;

import com.example.erp_system.model.OutgoingProductFromInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface OutgoingProductFromInventoryRepository extends JpaRepository<OutgoingProductFromInventory, Integer> {
    Optional<OutgoingProductFromInventory> findOutgoingProductFromInventoryByUpdatedAtBetween(Instant start, Instant end);

}
