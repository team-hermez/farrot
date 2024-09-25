package com.hermez.farrot.payment.adapter;

import com.hermez.farrot.payment.dto.request.PurchaseConfirmRequest;
import com.hermez.farrot.payment.dto.request.TrackingRequest;
import com.hermez.farrot.payment.dto.response.PaymentResponse;
import com.hermez.farrot.payment.dto.response.TrackingInfoResponse;


public interface PaymentAdapter {

    PaymentResponse initPayment();

    boolean confirmPurchase(PurchaseConfirmRequest request);

    TrackingInfoResponse registerLogisticsInfo(TrackingRequest request, String merchantId);

}
