package com.myroom.paymentservice.controller;

import com.myroom.paymentservice.api.model.*;
import com.myroom.paymentservice.api.resource.PaymentResource;
import com.myroom.paymentservice.usecase.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@Slf4j
public class PaymentController implements PaymentResource {
    @Autowired
    PaymentService paymentOrderService;

    @Override
    public ResponseEntity<PaymentOrderResponseModel> createPaymentOrder(PaymentOrderRequestModel paymentOrderRequestModel) {
        log.info("Creating payment order for: {}", paymentOrderRequestModel);
        PaymentOrderResponseModel paymentOrderResponseModel = paymentOrderService.createOrder(paymentOrderRequestModel);
        log.info("Created payment order: {}", paymentOrderResponseModel);
        return new ResponseEntity<>(paymentOrderResponseModel, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<PaymentDetailsResponseModel> getPaymentDetailsByBookingId(String bookingId) {
        log.info("Fetching the Payment details for bookingId: {}", bookingId);
        PaymentDetailsResponseModel paymentDetails = paymentOrderService.getPaymentDetailsByBookingId(bookingId);
        log.info("Fetched Payment details: {}", paymentDetails);
        return new ResponseEntity<>(paymentDetails, HttpStatus.OK);
    }

    @Override
    public RedirectView handlePaymentOrderSuccess(String sessionId, String type) {
        log.info("Received verifying Payment Success request");
        log.info("sessionId: {}, type: {}", sessionId, type);
        return new RedirectView("https://leetcode.com");
    }
}
