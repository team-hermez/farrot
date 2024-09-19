package com.hermez.farrot.payment.repository;

import com.hermez.farrot.payment.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentStatusRepository extends JpaRepository<PaymentStatus, Integer> {

}
