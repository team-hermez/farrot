package com.hermez.farrot.payment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentFormRequest {
    private Integer buyerId;
    private Integer productId;
    private Integer amount;
    private String productName;
}
