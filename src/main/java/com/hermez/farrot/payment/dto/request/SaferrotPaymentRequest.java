package com.hermez.farrot.payment.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SaferrotPaymentRequest {
    String serverName;
    String apiKey;
    String callbackUrl;
    Integer buyerId;
    Integer productId;
    String sellerAccount;
    int safeDay;
    String merchantUid;
    String paymentUrl;
    Integer amount;
    String productName;
    String resultUrl;
    String buyerEmail;

}
