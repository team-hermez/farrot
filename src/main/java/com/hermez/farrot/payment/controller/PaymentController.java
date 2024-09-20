package com.hermez.farrot.payment.controller;

import com.hermez.farrot.payment.dto.request.TrackingRequest;
import com.hermez.farrot.payment.dto.request.PaymentFormRequest;
import com.hermez.farrot.payment.dto.request.PurchaseConfirmRequest;
import com.hermez.farrot.payment.dto.response.PaymentResultResponse;
import com.hermez.farrot.payment.service.PaymentService;
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
    public String initPayment(@ModelAttribute PaymentFormRequest paymentFormRequest, Model model) {
        model.addAttribute("request",paymentService.initPayment(paymentFormRequest));
        return "payment/redirect-form";
    }

    @PostMapping("/payment-result")
    public ResponseEntity<Void> paymentComplete(@RequestBody PaymentResultResponse paymentResultResponse) {
        paymentService.completePayment(paymentResultResponse);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/confirm-purchase")
    public String confirmPurchase(@ModelAttribute PurchaseConfirmRequest request) {
        request.setBuyerId("1");
        paymentService.confirmPurchase(request);
        return "index/index";
    }

    @GetMapping("/member-payments")
    public String getPaymentsPage(Model model) {
        Integer memberId = 1;
        model.addAttribute("payments", paymentService.getPaymentsByMemberId(memberId));
        return "/payment/member-payments";
    }

    @GetMapping("/member-payments-sell")
    public String getPaymentsSellPage(Model model) {
        Integer memberId = 1;
        model.addAttribute("payments", paymentService.getPaymentsBySellerId(memberId));
        return "/payment/member-payments-sell";
    }

    @PostMapping("/register-logis")
    public String registerLogistics(@ModelAttribute TrackingRequest request) {
        paymentService.registerLogisticsInfo(request);
        return "redirect:/payment/member-payments-sell";
    }

    @PostMapping("/request-shipment-tracking")
    public void requestShipmentTracking(@RequestParam String merchantUid) {
        paymentService.showShipmentTracking(merchantUid);
    }
}
