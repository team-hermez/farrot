package com.hermez.farrot.payment.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
public class TrackingEscrowRequest {
    private Integer paymentId;
    private String courierCode;
    private String trackingNumber;
    private String serverName;
    private String merchantId;
    private String apiKey;
}
