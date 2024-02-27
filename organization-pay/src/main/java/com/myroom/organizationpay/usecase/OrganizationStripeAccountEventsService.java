package com.myroom.organizationpay.usecase;

import com.stripe.model.StripeObject;


public interface OrganizationStripeAccountEventsService {
    void handleAccountUpdatedEvent(StripeObject stripeObject);
}
