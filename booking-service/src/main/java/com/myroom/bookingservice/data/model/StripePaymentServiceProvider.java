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
public class StripePaymentServiceProvider{
    private String id;

    private String amount;

    private StripePaymentStatus status;

    private String url;

    public enum StripePaymentStatus{
        open,
        complete,
        expired
    }
}