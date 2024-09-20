package com.hermez.farrot.product.repository;

import com.hermez.farrot.category.entity.Category;
import com.hermez.farrot.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product> {

    Page<Product> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<Product> findByCategoryOrderByCreatedAtDesc(Category category, Pageable pageable);

    Page<Product> findByMemberIdOrderByCreatedAtDesc(Integer memberId, Pageable pageable);

    List<Product> findByMemberId(Integer memberId);

    Page<Product> findByProductNameContainingIgnoreCase(String productName, Pageable pageable);

    List<Product> findTop5ByOrderByCreatedAtDesc();
}
