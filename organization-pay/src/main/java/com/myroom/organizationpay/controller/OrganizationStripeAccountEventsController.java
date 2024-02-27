package com.myroom.organizationpay.controller;

import com.myroom.organizationpay.api.resource.OrganizationStripeAccountEventsResource;
import com.myroom.organizationpay.usecase.StripeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
@Slf4j
public class OrganizationStripeAccountEventsController implements OrganizationStripeAccountEventsResource {
    @Autowired
    StripeService stripeService;


    @Override
    public ResponseEntity updateAccountEvent(String payload, Map<String, String> headers) {
        log.info("Received stripe webhook event");
        stripeService.handleEvent(payload, headers);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}