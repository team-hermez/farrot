package com.hermez.farrot.payment.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaymentFormRequest {
    private Integer buyerId;
    private Integer productId;
    private Integer amount;
    private String productName;
}
