package com.myroom.bookingservice.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.myroom.bookingservice.api.constants.PaymentMethodType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentOrderRequestDto {
    private PaymentMethodType paymentMethodType;

    private String amount;

    private String currency;

    private String bookingId;

    private String receipt;

    private String organizationId;

    private String roomId;

    private String roomTitle;

    private String uid;
}