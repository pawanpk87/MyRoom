package com.myroom.paymentservice.data.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StripePaymentOrderResponseModel {
    private String id;

    private String url;

    private String status;

    private String amount;
}