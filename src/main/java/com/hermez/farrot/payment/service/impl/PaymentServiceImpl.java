package com.hermez.farrot.payment.service.impl;

import com.hermez.farrot.payment.adapter.PaymentAdapter;
import com.hermez.farrot.payment.dto.request.PaymentFormRequest;
import com.hermez.farrot.payment.dto.request.PurchaseConfirmRequest;
import com.hermez.farrot.payment.dto.request.SaferrotPaymentRequest;
import com.hermez.farrot.payment.dto.response.PaymentResultResponse;
import com.hermez.farrot.payment.entity.Payment;
import com.hermez.farrot.payment.entity.PaymentStatus;
import com.hermez.farrot.payment.repository.PaymentRepository;
import com.hermez.farrot.payment.repository.PaymentStatusRepository;
import com.hermez.farrot.payment.service.PaymentService;
import com.hermez.farrot.product.service.ProductService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentAdapter paymentAdapter;
    private final PaymentRepository paymentRepository;
    private final PaymentStatusRepository paymentStatusRepository;
    private final ProductService productService;

    public PaymentServiceImpl(PaymentAdapter paymentAdapter, PaymentRepository paymentRepository, PaymentStatusRepository paymentStatusRepository, ProductService productService) {
        this.paymentAdapter = paymentAdapter;
        this.paymentRepository = paymentRepository;
        this.paymentStatusRepository = paymentStatusRepository;
        this.productService = productService;
    }

    @Value("${safe-payment.api.key}")
    protected String apiKey;


    @Override
    public SaferrotPaymentRequest initPayment(PaymentFormRequest paymentFormRequest) {
        return SaferrotPaymentRequest.builder()
                .serverName("farrot")
                .apiKey(apiKey)
                .callbackUrl("http://localhost:8080/product/product-detail?productId=204")
                .buyerId(1)
                .productId(1)
                .sellerAccount("0000-0000-0000-0000")
                .safeDay(5)
                .merchantUid("merchant_" + new Date().getTime())
                .paymentUrl(paymentAdapter.initPayment().getPaymentUrl())
                .amount(10)
                .productName("상품1")
                .resultUrl("http://localhost:8080/payment/payment-result")
                .buyerEmail("aaa@aaa")
                .build();
    }

    @Transactional
    @Override
    public void completePayment(PaymentResultResponse paymentResultResponse) {
        PaymentStatus paymentStatus = paymentStatusRepository.findById(1)
                .orElseThrow(() -> new IllegalArgumentException("결제 상태를 찾을 수 없습니다."));

        Payment payment = Payment.builder()
                .product(productService.getProductById(Integer.parseInt(paymentResultResponse.getProductId())))
                .merchantUid(paymentResultResponse.getMerchantUid())
                .escrowCode(paymentResultResponse.getEscrowCode())
                .memberId(Integer.parseInt(paymentResultResponse.getBuyerId()))
                .paymentStatus(paymentStatus)
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .build();

        paymentRepository.save(payment);
        productService.updateProductStatus(payment.getProduct().getId(),2);
    }

    @Override
    public void confirmPurchase(PurchaseConfirmRequest request) {
       paymentAdapter.confirmPurchase(request);
    }

    @Override
    public List<Payment> getPaymentsByMemberId(Integer memberId) {
        return paymentRepository.findByMemberId(memberId);
    }
}
