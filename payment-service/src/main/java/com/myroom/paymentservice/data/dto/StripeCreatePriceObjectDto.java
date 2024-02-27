package com.myroom.paymentservice.data.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class StripeCreatePriceObjectDto {
    private String bookingId;

    private String roomId;

    private String title;

    private String uid;

    private BigDecimal amount;

    private String currency;

    private String receipt;

    private String organizationId;
}
