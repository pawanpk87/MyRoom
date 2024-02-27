package com.myroom.paymentservice.usecase;

import com.stripe.model.StripeObject;

public interface StripePaymentEventService {
    void handlePaymentCompletedUpdatedEvent(StripeObject stripeObject);
}