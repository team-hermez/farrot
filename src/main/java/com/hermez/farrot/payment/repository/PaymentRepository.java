package com.hermez.farrot.payment.repository;

import com.hermez.farrot.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository <Payment, Integer> {


    List<Payment> findByMemberId(Integer memberId);

}
