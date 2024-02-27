package com.myroom.paymentservice.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.myroom.paymentservice.api.constants.PaymentMethodType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentOrderRequestModel {
    @Schema(description = "payment method type", example = "STRIPE", required = true)
    private PaymentMethodType paymentMethodType;

    @Schema(description = "bookingId", example = "13124", required = true)
    private String bookingId;

    @Schema(description = "roomId", example = "87134", required = true)
    private String roomId;

    @Schema(description = "roomTitle", example = "87134", required = true)
    private String roomTitle;

    @Schema(description = "user id", example = "901237412412", required = true)
    private String uid;

    @Schema(description = "amount", example = "299", required = true)
    private BigDecimal amount;

    @Schema(description = "currency", example = "INR", required = true)
    private String currency;

    @Schema(description = "receipt", example = "receipt#1", required = true)
    private String receipt;

    @Schema(description = "organizationId", example = "13124", required = true)
    private String organizationId;
}