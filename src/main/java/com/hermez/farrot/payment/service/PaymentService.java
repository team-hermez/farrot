package com.hermez.farrot.payment.service;

import com.hermez.farrot.payment.dto.request.TrackingRequest;
import com.hermez.farrot.payment.dto.request.PaymentFormRequest;
import com.hermez.farrot.payment.dto.request.PurchaseConfirmRequest;
import com.hermez.farrot.payment.dto.request.SaferrotPaymentRequest;
import com.hermez.farrot.payment.dto.response.PaymentResultResponse;
import com.hermez.farrot.payment.entity.Payment;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PaymentService {

    SaferrotPaymentRequest initPayment(PaymentFormRequest paymentFormRequest, HttpServletRequest servletRequest);

    void completePayment(PaymentResultResponse paymentResultResponse);

    void confirmPurchase(PurchaseConfirmRequest request, HttpServletRequest servletRequest);

    Page<Payment> getPaymentsByMemberId(Integer memberId, int page, int size);

    Page<Payment> getPaymentsByLoginMemberId(HttpServletRequest servletRequest, int page, int size);

    Page<Payment> getPaymentsBySellerId(HttpServletRequest servletRequest, int page, int size);

    void registerLogisticsInfo(TrackingRequest request);

}
