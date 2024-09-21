package com.hermez.farrot.payment.adapter.impl;

import com.hermez.farrot.payment.adapter.PaymentAdapter;
import com.hermez.farrot.payment.dto.request.PurchaseConfirmRequest;
import com.hermez.farrot.payment.dto.request.TrackingEscrowRequest;
import com.hermez.farrot.payment.dto.request.TrackingRequest;
import com.hermez.farrot.payment.dto.response.PaymentResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Component
public class PaymentAdapterImpl implements PaymentAdapter {

    @Value("${safe-payment.api.url}")
    protected String url;

    @Value("${safe-payment.api.key}")
    protected String apiKey;

    private final RestTemplate restTemplate;

    public PaymentAdapterImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public PaymentResponse initPayment() {
        try {
            ResponseEntity<PaymentResponse> response = restTemplate.postForEntity(
                    url + "/escrow/initiate-payment",
                    apiKey,
                    PaymentResponse.class
            );

            return response.getBody();
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "인증받지 못한 API Key입니다.");
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "결제 요청 중 오류가 발생했습니다.");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다.");
        }
    }

    @Override
    public void confirmPurchase(PurchaseConfirmRequest request) {
        try {
            HttpEntity<PurchaseConfirmRequest> httpEntity = new HttpEntity<>(request);

            ResponseEntity<String> response = restTemplate.exchange(
                    url + "/escrow/confirm",
                    HttpMethod.POST,
                    httpEntity,
                    String.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.println("결제 확정 요청이 성공적으로 처리되었습니다.");
            } else {
                System.out.println("결제 확정 요청 실패: " + response.getStatusCode());
            }
        } catch (HttpClientErrorException e) {
            throw new ResponseStatusException(e.getStatusCode(), "결제 확정 요청 중 오류가 발생했습니다.");
        } catch (RestClientException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 통신 중 오류가 발생했습니다.");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "예기치 않은 서버 오류가 발생했습니다.");
        }
    }

    @Override
    public void registerLogisticsInfo(TrackingRequest request,String merchantId) {
        try {
            HttpEntity<TrackingEscrowRequest> httpEntity = new HttpEntity<>(TrackingEscrowRequest.builder()
                    .courierCode(request.getCourierCode())
                    .paymentId(request.getPaymentId())
                    .trackingNumber(request.getTrackingNumber())
                    .merchantId(merchantId)
                    .serverName("farrot")
                    .apiKey(apiKey)
                    .build());

            ResponseEntity<String> response = restTemplate.exchange(
                    url + "/api/shipping/register-logistics",
                    HttpMethod.POST,
                    httpEntity,
                    String.class
            );
            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.println("운송장 등록 요청이 성공적으로 처리되었습니다.");
            } else {
                System.out.println("운송장 등록 요청 실패: " + response.getStatusCode());
            }
        } catch (HttpClientErrorException e) {
            throw new ResponseStatusException(e.getStatusCode(), "등록되지 않은 운송장 번호 입니다.");
        } catch (RestClientException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 통신 중 오류가 발생했습니다.");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "예기치 않은 서버 오류가 발생했습니다.");
        }
    }

}
