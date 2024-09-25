package com.hermez.farrot.product.repository;

import com.hermez.farrot.product.entity.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductStatusRepository extends JpaRepository<ProductStatus,Integer> {
    Optional<ProductStatus> findByStatus(String status);

}
