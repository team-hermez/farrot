package com.hermez.farrot.payment.adapter;

import com.hermez.farrot.payment.dto.request.PurchaseConfirmRequest;
import com.hermez.farrot.payment.dto.request.TrackingRequest;
import com.hermez.farrot.payment.dto.response.PaymentResponse;


public interface PaymentAdapter {

    PaymentResponse initPayment();

    void confirmPurchase(PurchaseConfirmRequest request);

    void registerLogisticsInfo(TrackingRequest request,String merchantId);

}
