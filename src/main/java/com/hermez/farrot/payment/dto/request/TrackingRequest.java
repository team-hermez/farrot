package com.hermez.farrot.payment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrackingRequest {
    private Integer paymentId;
    private String courierCode;
    private String trackingNumber;
}