package com.hermez.farrot.payment.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseConfirmRequest {
    private String merchantUid;
    private String paymentId;
    private String buyerId;
}
