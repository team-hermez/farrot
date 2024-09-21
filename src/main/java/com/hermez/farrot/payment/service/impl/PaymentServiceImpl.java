package com.hermez.farrot.payment.service.impl;

import com.google.api.Http;
import com.hermez.farrot.member.entity.Member;
import com.hermez.farrot.member.repository.MemberRepository;
import com.hermez.farrot.member.service.MemberService;
import com.hermez.farrot.payment.adapter.PaymentAdapter;
import com.hermez.farrot.payment.dto.request.TrackingRequest;
import com.hermez.farrot.payment.dto.request.PaymentFormRequest;
import com.hermez.farrot.payment.dto.request.PurchaseConfirmRequest;
import com.hermez.farrot.payment.dto.request.SaferrotPaymentRequest;
import com.hermez.farrot.payment.dto.response.PaymentResultResponse;
import com.hermez.farrot.payment.entity.Payment;
import com.hermez.farrot.payment.entity.PaymentStatus;
import com.hermez.farrot.payment.repository.PaymentRepository;
import com.hermez.farrot.payment.repository.PaymentStatusRepository;
import com.hermez.farrot.payment.service.PaymentService;
import com.hermez.farrot.product.entity.Product;
import com.hermez.farrot.product.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
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
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    public PaymentServiceImpl(PaymentAdapter paymentAdapter, PaymentRepository paymentRepository, PaymentStatusRepository paymentStatusRepository, ProductService productService, MemberRepository memberRepository, MemberService memberService) {
        this.paymentAdapter = paymentAdapter;
        this.paymentRepository = paymentRepository;
        this.paymentStatusRepository = paymentStatusRepository;
        this.productService = productService;
        this.memberRepository = memberRepository;
        this.memberService = memberService;
    }

    @Value("${safe-payment.api.key}")
    protected String apiKey;

    @Override
    public SaferrotPaymentRequest initPayment(PaymentFormRequest paymentFormRequest, HttpServletRequest servletRequest) {
        Member buyerMember = memberService.getMember(servletRequest);
        Product product = productService.getProductById(paymentFormRequest.getProductId());
        Member sellerMember = memberRepository.findById(product.getMember().getId())
                .orElseThrow(() -> new EntityNotFoundException("판매자 정보가 없습니다"));

        return SaferrotPaymentRequest.builder()
                .serverName("farrot")
                .apiKey(apiKey)
                .callbackUrl("http://localhost:8080/product/product-detail?productId=" + paymentFormRequest.getProductId())
                .buyerId(buyerMember.getId())
                .productId(product.getId())
                .sellerId(sellerMember.getId())
                .sellerEmail(sellerMember.getEmail())
                .sellerAccount("3020000011519")
                .safeDay(5)
                .merchantUid("merchant_" + new Date().getTime())
                .paymentUrl(paymentAdapter.initPayment().getPaymentUrl())
                .amount(product.getPrice())
                .productName(product.getProductName())
                .resultUrl("http://localhost:8080/payment/payment-result")
                .buyerEmail(buyerMember.getEmail())
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
                .member(memberRepository.findById(Integer.parseInt(paymentResultResponse.getBuyerId())).orElseThrow(() ->
                        new IllegalArgumentException("멤버ID가 없습니다")))
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
    public List<Payment> getPaymentsByMemberId(HttpServletRequest servletRequest) {
        return paymentRepository.findByMemberId(memberService.getMember(servletRequest).getId());
    }

    @Override
    public List<Payment> getPaymentsBySellerId(HttpServletRequest servletRequest) {
        List<Product> products = productService.getProductsByMember(memberService.getMember(servletRequest).getId());
        List<Payment> payments = products.stream()
                .flatMap(product -> paymentRepository.findByProduct(product).stream())
                .toList();
        return payments;
    }

    @Override
    public void registerLogisticsInfo(TrackingRequest request) {
        Payment payment = paymentRepository.findById(request.getPaymentId())
                .orElseThrow(() -> new IllegalArgumentException("없는 id입니다"));
        paymentAdapter.registerLogisticsInfo(request, payment.getMerchantUid());
        payment.setCourierCode(request.getCourierCode());
        payment.setTrackingNumber(request.getTrackingNumber());
        paymentStatusRepository.findById(2).ifPresent(payment::setPaymentStatus);
        paymentRepository.save(payment);
        Product product = payment.getProduct();
        productService.updateProductStatus(product.getId(),2);
    }

}
