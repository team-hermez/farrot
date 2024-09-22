package com.hermez.farrot.payment.controller;

import com.hermez.farrot.payment.dto.request.TrackingRequest;
import com.hermez.farrot.payment.dto.request.PaymentFormRequest;
import com.hermez.farrot.payment.dto.request.PurchaseConfirmRequest;
import com.hermez.farrot.payment.dto.response.PaymentResultResponse;
import com.hermez.farrot.payment.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/init-payment")
    public String initPayment(@ModelAttribute PaymentFormRequest paymentFormRequest, Model model, HttpServletRequest servletRequest) {
        model.addAttribute("request", paymentService.initPayment(paymentFormRequest, servletRequest));
        return "payment/redirect-form";
    }

    @PostMapping("/payment-result")
    public ResponseEntity<Void> paymentComplete(@RequestBody PaymentResultResponse paymentResultResponse) {
        paymentService.completePayment(paymentResultResponse);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/confirm-purchase")
    public String confirmPurchase(@ModelAttribute PurchaseConfirmRequest request, HttpServletRequest servletRequest) {
        paymentService.confirmPurchase(request, servletRequest);
        return "index/index";
    }

    @GetMapping("/member-payments")
    public String getPaymentsPage(Model model,HttpServletRequest servletRequest) {
        model.addAttribute("payments", paymentService.getPaymentsByMemberId(servletRequest));
        return "/payment/member-payments-buy";
    }

    @GetMapping("/member-payments-sell")
    public String getPaymentsSellPage(Model model, HttpServletRequest servletRequest) {
        model.addAttribute("payments", paymentService.getPaymentsBySellerId(servletRequest));
        return "/payment/member-payments-sell";
    }

    @PostMapping("/register-logis")
    public String registerLogistics(@ModelAttribute TrackingRequest request) {
        paymentService.registerLogisticsInfo(request);
        return "redirect:/payment/member-payments-sell";
    }
}
