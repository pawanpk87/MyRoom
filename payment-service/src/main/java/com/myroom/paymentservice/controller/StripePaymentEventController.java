package com.myroom.paymentservice.controller;

import com.myroom.paymentservice.api.resource.StripePaymentEventResource;
import com.myroom.paymentservice.usecase.StripeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
public class StripePaymentEventController implements StripePaymentEventResource {
    @Autowired
    StripeService stripeService;

    @Override
    public ResponseEntity updatePaymentEvent(String payload, Map<String, String> headers) {
        log.info("Received stripe webhook event");
        stripeService.handleEvent(payload, headers);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}