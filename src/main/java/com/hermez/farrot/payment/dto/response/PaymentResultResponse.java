package com.hermez.farrot.payment.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentResultResponse {

    private String productId;
    private String merchantUid;
    private String escrowCode;
    private String buyerId;
    private String loadAddress;
    private String detailAddress;
    private String buyerPostcode;
}
