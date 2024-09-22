package com.hermez.farrot.payment.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "SHIPPING_PAYMENT")
public class ShippingPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SHIPPING_PAYMENT_ID")
    private Integer shippingPaymentId;

    @Column(name = "COURIER_CODE")
    private String courierCode;

    @Column(name = "TRACKING_NUMBER")
    private String trackingNumber;

    @Column(name = "SHIPPING_ADDRESS")
    private String shippingAddress;

    @Column(name = "POST_CODE")
    private String postCode;

    @Column(name = "SHIPPING_DETAIL")
    private String shippingDetail;

}