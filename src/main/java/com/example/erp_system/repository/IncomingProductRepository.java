package com.example.erp_system.repository;

import com.example.erp_system.model.IncomingProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IncomingProductRepository extends JpaRepository<IncomingProduct, Integer> {
    Optional<List<IncomingProduct>> findIncomingProductsByProductId(int productID);
}
