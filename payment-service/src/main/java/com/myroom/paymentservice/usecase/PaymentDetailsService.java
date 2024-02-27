package com.myroom.paymentservice.usecase;

import com.myroom.paymentservice.data.dto.StripePaymentOrderRequestModel;
import com.myroom.paymentservice.data.entity.PaymentDetails;
import com.stripe.model.checkout.Session;

public interface PaymentDetailsService {
    PaymentDetails savePayment(Session session, StripePaymentOrderRequestModel razorpayPaymentOrderRequestModel);
}
