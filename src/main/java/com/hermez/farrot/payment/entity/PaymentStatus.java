package com.hermez.farrot.payment.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "PAYMENT_STATUS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PAYMENT_STATUS_ID")
    private Integer paymentStatusId;

    @Column(name = "STATUS", nullable = false, length = 30)
    private String status;
}

