package com.myroom.paymentservice.usecase.impl;

import com.myroom.paymentservice.data.dto.*;
import com.myroom.paymentservice.data.entity.PaymentDetails;
import com.myroom.paymentservice.data.mapper.StripePaymentServiceMapper;
import com.myroom.paymentservice.data.mapper.StripeServiceMapper;
import com.myroom.paymentservice.exception.*;
import com.myroom.paymentservice.usecase.PaymentDetailsService;
import com.myroom.paymentservice.usecase.StripePaymentService;
import com.myroom.paymentservice.usecase.StripePaymentValidationService;
import com.myroom.paymentservice.usecase.StripeService;
import com.stripe.model.checkout.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StripePaymentServiceImpl implements StripePaymentService {
    @Autowired
    StripeService stripeService;

    @Autowired
    StripeServiceMapper stripeServiceMapper;

    @Autowired
    StripePaymentValidationService stripePaymentValidationService;

    @Autowired
    StripePaymentServiceMapper stripePaymentServiceMapper;

    @Autowired
    PaymentDetailsService paymentDetailsService;


    @Override
    public StripePaymentOrderResponseModel createOrder(StripePaymentOrderRequestModel stripePaymentOrderRequestModel) {
        log.info("Creating stripe payment order(Stripe Session) for: {}", stripePaymentOrderRequestModel);
        StripePaymentOrder stripePaymentOrder = validatePaymentOrderRequestAndGetStripePaymentOrder(stripePaymentOrderRequestModel);
        StripePaymentOrderResponseModel stripePaymentOrderResponseModel = stripePaymentServiceMapper.toStripePaymentOrderResponseModel(stripePaymentOrder);
        log.info("Created stripe payment order: {}", stripePaymentOrderResponseModel);
        return stripePaymentOrderResponseModel;
    }

    @Override
    public StripePaymentOrderSuccessResponse handlePaymentOrderSuccess(StripePaymentOrderSuccessRequest stripePaymentOrderSuccessRequest) {
        verifyPaymentSignature(stripePaymentOrderSuccessRequest);
        StripePaymentOrderSuccessResponse stripePaymentOrderSuccessResponse = StripePaymentOrderSuccessResponse.builder()
                .message("Payment successfully!")
                .build();
        return stripePaymentOrderSuccessResponse;
    }

    private void verifyPaymentSignature(StripePaymentOrderSuccessRequest stripePaymentOrderSuccessRequest){
        log.info("Verifying stripe payment success");

    }

    private StripePaymentOrder validatePaymentOrderRequestAndGetStripePaymentOrder(StripePaymentOrderRequestModel stripePaymentOrderRequestModel) {
        log.info("Validating PaymentOrderRequest");
        if(stripePaymentValidationService.validateOrganizationAccountActive(stripePaymentOrderRequestModel.getOrganizationId())){
            log.info("Validated PaymentOrderRequest");
            StripePaymentOrder stripePaymentOrder = createStripePaymentOrder(stripePaymentOrderRequestModel);
            return stripePaymentOrder;
        }else{
            log.error("Organization account is not active");
            throw new PaymentServiceRuntimeException("Payment processing is currently unavailable as the organization's account is inactive.");
        }
    }

    private StripePaymentOrder createStripePaymentOrder(StripePaymentOrderRequestModel stripePaymentOrderRequestModel) {
        log.info("Create Stripe Payment Orders");
        StripeCreateSessionObjectDto stripeCreateSessionObjectDto = stripePaymentServiceMapper.toStripeCreateSessionObjectDto(stripePaymentOrderRequestModel);
        Session session = stripeService.createSession(stripeCreateSessionObjectDto);
        log.info("Created Stripe Payment order: {}", session);

        log.info("Saving payment details");
        PaymentDetails paymentDetails = paymentDetailsService.savePayment(session, stripePaymentOrderRequestModel);
        log.info("Saved payment details: {}", paymentDetails);

        return stripeServiceMapper.toStripePaymentOrder(session, stripePaymentOrderRequestModel);
    }
}