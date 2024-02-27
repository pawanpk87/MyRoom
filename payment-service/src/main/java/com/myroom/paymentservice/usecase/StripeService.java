package com.myroom.paymentservice.usecase;

import com.myroom.paymentservice.data.dto.StripeCreateSessionObjectDto;
import com.stripe.model.checkout.Session;

import java.util.Map;

public interface StripeService {
    Session createSession(StripeCreateSessionObjectDto stripeCreateSessionObjectDto);

    void handleEvent(String payload, Map<String, String> headers);
}