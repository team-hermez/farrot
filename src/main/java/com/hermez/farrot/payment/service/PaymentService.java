package com.hermez.farrot.payment.service;

import com.hermez.farrot.payment.dto.request.TrackingRequest;
import com.hermez.farrot.payment.dto.request.PaymentFormRequest;
import com.hermez.farrot.payment.dto.request.PurchaseConfirmRequest;
import com.hermez.farrot.payment.dto.request.SaferrotPaymentRequest;
import com.hermez.farrot.payment.dto.response.PaymentResultResponse;
import com.hermez.farrot.payment.entity.Payment;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface PaymentService {

    SaferrotPaymentRequest initPayment(PaymentFormRequest paymentFormRequest, HttpServletRequest servletRequest);

    void completePayment(PaymentResultResponse paymentResultResponse);

    void confirmPurchase(PurchaseConfirmRequest request);

    List<Payment> getPaymentsByMemberId(HttpServletRequest servletRequest);

    List<Payment> getPaymentsBySellerId(HttpServletRequest servletRequest);

    void registerLogisticsInfo(TrackingRequest request);

}
