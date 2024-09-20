package com.hermez.farrot.payment.entity;

import com.hermez.farrot.member.entity.Member;
import com.hermez.farrot.product.entity.Product;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Table(name = "PAYMENT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PAYMENT_ID")
    private Integer paymentId;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "PAYMENT_STATUS_ID", nullable = false)
    private PaymentStatus paymentStatus;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    @Column(name = "CREATED_AT")
    private Timestamp createdAt;

    @Column(name = "FINISH_AT")
    private Timestamp finishAt;

    @Column(name = "MERCHANT_UID")
    private String merchantUid;

    @Column(name = "ESCROW_CODE")
    private String escrowCode;

    @Column(name = "COURIER_CODE")
    private String courierCode;

    @Column(name = "TRACKING_NUMBER")
    private String trackingNumber;

}
