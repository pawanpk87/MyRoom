package com.myroom.paymentservice.usecase.impl;

import com.myroom.paymentservice.api.constants.ApiConstants;
import com.myroom.paymentservice.data.dto.StripePaymentOrderRequestModel;
import com.myroom.paymentservice.data.entity.PaymentDetails;
import com.myroom.paymentservice.data.mapper.StripePaymentServiceMapper;
import com.myroom.paymentservice.exception.PaymentServiceRuntimeException;
import com.myroom.paymentservice.repository.PaymentDetailsRepository;
import com.myroom.paymentservice.usecase.PaymentDetailsService;
import com.stripe.model.checkout.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaymentDetailsServiceImpl implements PaymentDetailsService {
    @Autowired
    PaymentDetailsRepository paymentDetailsRepository;

    @Autowired
    StripePaymentServiceMapper stripePaymentServiceMapper;


    @Override
    public PaymentDetails savePayment(Session session, StripePaymentOrderRequestModel stripePaymentOrderRequestModel) {
        PaymentDetails paymentDetails = stripePaymentServiceMapper.toPaymentDetails(session, stripePaymentOrderRequestModel);
        try {
            return paymentDetailsRepository.save(paymentDetails);
        }catch (Exception ex){
            log.error("Some error curred: {}", ex.getMessage());
            throw new PaymentServiceRuntimeException(ApiConstants.MESSAGE_INTERNAL_SERVER_ERROR);
        }
    }
}