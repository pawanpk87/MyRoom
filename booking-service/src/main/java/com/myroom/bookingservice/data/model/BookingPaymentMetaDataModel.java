package com.myroom.bookingservice.data.model;

import com.myroom.bookingservice.api.constants.PaymentMethodType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingPaymentMetaDataModel {
    private PaymentMethodType paymentMethodType;

    private String amount;

    private String currency;

    private StripePaymentServiceProvider stripePaymentServiceProvider;
}
