package com.hermez.farrot.payment.dto.request;

import lombok.Data;

@Data
public class PaymentCompleteRequest {
    private String paymentId;
    private String status;
    private String buyerId;

}
