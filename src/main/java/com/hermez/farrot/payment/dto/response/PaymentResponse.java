package com.hermez.farrot.payment.dto.response;

import lombok.Data;

@Data
public class PaymentResponse {
    private String paymentId;
    private String paymentUrl;
}