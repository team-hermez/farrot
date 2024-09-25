package com.hermez.farrot.payment.repository;

import com.hermez.farrot.payment.entity.Payment;
import com.hermez.farrot.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    List<Payment> findByMemberId(Integer memberId);

    List<Payment> findByProduct(Product product);

    Optional<Payment> findByMerchantUid(String merchantUid);

    Page<Payment> findByMemberId(Integer memberId, Pageable pageable);

    Page<Payment> findByProduct(Product product, Pageable pageable);

    Page<Payment> findByProductIdIn(List<Integer> productIds, Pageable pageable);


}
