package com.example.erp_system.repository;

import com.example.erp_system.model.OutgoingProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutgoingProductRepository extends JpaRepository<OutgoingProduct, Integer> {

}
