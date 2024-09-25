package com.hermez.farrot.payment.repository;

import com.hermez.farrot.payment.entity.ShippingPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShippingPaymentRepository extends JpaRepository<ShippingPayment, Integer> {
}
