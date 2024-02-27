package com.myroom.bookingservice.usecase.impl;

import com.myroom.bookingservice.data.dto.PaymentOrderRequestDto;
import com.myroom.bookingservice.data.dto.PaymentOrderResponseDto;
import com.myroom.bookingservice.exception.BookingServiceRuntimeException;
import com.myroom.bookingservice.exception.PaymentServiceException;
import com.myroom.bookingservice.usecase.PaymentService;
import com.myroom.bookingservice.utils.EurekaClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    EurekaClientUtil eurekaClientUtil;

    @Autowired
    WebClient webClient;

    @Value("${services.payment-service.id}")
    String paymentServiceID;

    @Value("${services.payment-service.v1.api}")
    String paymentServiceV1;

    @Value("${services.payment-service.v1.name}")
    String paymentServiceV1Name;

    @Override
    public PaymentOrderResponseDto createPaymentOrder(PaymentOrderRequestDto paymentOrderRequestDto) {
        log.info("Calling {} Service for creating payment order: {}", paymentServiceV1Name, paymentOrderRequestDto);

        PaymentOrderResponseDto paymentOrder = null;

        // Construct URL
        String paymentServiceUri = eurekaClientUtil.getServiceUri(paymentServiceID);
        String url = paymentServiceUri + paymentServiceV1 + "/orders";

        try {
            PaymentOrderResponseDto paymentOrderResponse = webClient.post()
                    .uri(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(paymentOrderRequestDto)
                    .retrieve()
                    .bodyToMono(PaymentOrderResponseDto.class)
                    .block();

            log.info("Payment Order created successfully: {}", paymentOrderResponse);

            paymentOrder = paymentOrderResponse;
        }catch (WebClientResponseException webClientResponseException){
            handleWebClientException(webClientResponseException);
        }catch (Exception ex){
            log.error("Some error curred: {}", ex.getMessage());
            throw new BookingServiceRuntimeException(ex.getMessage());
        }

        return paymentOrder;
    }

    private void handleWebClientException(WebClientResponseException webClientResponseException) {
        log.info("Handling WebClientException");

        // Convert error response to JSONObject
        JSONObject response = new JSONObject(webClientResponseException.getResponseBodyAsString());

        log.info("Error response from {} Service is: {}", paymentServiceV1Name, response);

        String errorCode = response.getString("errorCode");
        String message = response.getString("message");
        String details = response.getString("details");

        throw new PaymentServiceException(errorCode, message, details);
    }
}