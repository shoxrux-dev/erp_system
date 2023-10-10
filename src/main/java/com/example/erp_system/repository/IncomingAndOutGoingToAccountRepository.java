package com.example.erp_system.repository;

import com.example.erp_system.model.IncomingAndOutgoingToAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface IncomingAndOutGoingToAccountRepository extends JpaRepository<IncomingAndOutgoingToAccount, Integer> {
    Optional<IncomingAndOutgoingToAccount> findIncomingAndOutgoingToAccountByUpdatedAtBetween(Instant start, Instant end);

}
