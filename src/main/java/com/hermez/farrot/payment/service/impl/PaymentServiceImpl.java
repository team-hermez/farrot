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
import com.hermez.farrot.payment.dto.response.TrackingInfoResponse;
import com.hermez.farrot.payment.entity.Payment;
import com.hermez.farrot.payment.entity.PaymentStatus;
import com.hermez.farrot.payment.entity.ShippingPayment;
import com.hermez.farrot.payment.repository.PaymentRepository;
import com.hermez.farrot.payment.repository.PaymentStatusRepository;
import com.hermez.farrot.payment.repository.ShippingPaymentRepository;
import com.hermez.farrot.payment.service.PaymentService;
import com.hermez.farrot.product.entity.Product;
import com.hermez.farrot.product.repository.ProductRepository;
import com.hermez.farrot.product.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final ShippingPaymentRepository shippingPaymentRepository;
    private final ProductRepository productRepository;

    public PaymentServiceImpl(PaymentAdapter paymentAdapter, PaymentRepository paymentRepository, PaymentStatusRepository paymentStatusRepository, ProductService productService, MemberRepository memberRepository, MemberService memberService, ShippingPaymentRepository shippingPaymentRepository, ProductRepository productRepository) {
        this.paymentAdapter = paymentAdapter;
        this.paymentRepository = paymentRepository;
        this.paymentStatusRepository = paymentStatusRepository;
        this.productService = productService;
        this.memberRepository = memberRepository;
        this.memberService = memberService;
        this.shippingPaymentRepository = shippingPaymentRepository;
        this.productRepository = productRepository;
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
        productService.updateProductStatus(payment.getProduct().getId(), 2);
    }

    @Override
    public void confirmPurchase(PurchaseConfirmRequest request, HttpServletRequest servletRequest) {
        request.setBuyerId(memberService.getMember(servletRequest).getId().toString());
        boolean isConfirmPurchase = paymentAdapter.confirmPurchase(request);
        if(isConfirmPurchase){
            Payment payment = paymentRepository.findByMerchantUid(request.getMerchantUid()).orElseThrow(() ->
                    new IllegalArgumentException("결제를 찾을 수 없습니다."));
            paymentStatusRepository.findById(4).ifPresent(payment::setPaymentStatus);
            productService.updateProductStatus(payment.getProduct().getId(),3);
            paymentRepository.save(payment);
        }
    }

    @Override
    public Page<Payment> getPaymentsByMemberId(Integer memberId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "paymentId"));
        return paymentRepository.findByMemberId(memberId, pageable);
    }

    @Override
    public Page<Payment> getPaymentsByLoginMemberId(HttpServletRequest servletRequest, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "paymentId"));
        return paymentRepository.findByMemberId(memberService.getMember(servletRequest).getId(), pageable);
    }

    @Override
    public Page<Payment> getPaymentsBySellerId(HttpServletRequest servletRequest, int page, int size) {
        List<Product> products = productService.getProductsByMember(memberService.getMember(servletRequest).getId());
        List<Integer> productIds = products.stream().map(Product::getId).toList();

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "paymentId"));
        return paymentRepository.findByProductIdIn(productIds, pageable);
    }

    @Override
    public void registerLogisticsInfo(TrackingRequest request) {
        Payment payment = paymentRepository.findById(request.getPaymentId())
                .orElseThrow(() -> new IllegalArgumentException("없는 id입니다"));

        TrackingInfoResponse trackingInfoResponse = paymentAdapter.registerLogisticsInfo(request, payment.getMerchantUid());

        ShippingPayment shippingPayment = new ShippingPayment();
        shippingPayment.setCourierCode(request.getCourierCode());
        shippingPayment.setTrackingNumber(request.getTrackingNumber());

        shippingPaymentRepository.save(shippingPayment);

        payment.setShippingPayment(shippingPayment);

        if (trackingInfoResponse.getCompleteYN().equals("N")) {
            paymentStatusRepository.findById(2).ifPresent(payment::setPaymentStatus);
        }
        if (trackingInfoResponse.getCompleteYN().equals("Y")) {
            paymentStatusRepository.findById(3).ifPresent(payment::setPaymentStatus);
        }
        paymentRepository.save(payment);

        Product product = payment.getProduct();
        productService.updateProductStatus(product.getId(), 2);
    }
}
