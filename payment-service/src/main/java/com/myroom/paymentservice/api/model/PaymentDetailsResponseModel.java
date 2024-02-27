package com.myroom.paymentservice.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.myroom.paymentservice.api.constants.PaymentMethodType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentDetailsResponseModel {
    @Schema(description = "payment type", example = "STRIPE", required = true)
    private String id;

//    @Schema(description = "payment id", example = "cs_test_a1hzfOj0u9wL5JnnXYGBM8oWjNSKsTV5bdW66jlACOGh2ec4SpZurpiCBu", required = true)
//    private String paymentId;

    @Schema(description = "payment type", example = "STRIPE", required = true)
    PaymentMethodType type;

    @Schema(description = "amount", example = "1200", required = true)
    private BigDecimal amount;

    @Schema(description = "currency", example = "inr", required = true)
    String currency;

    @Schema(description = "bookingId", example = "bk_El440oZ8g9BNVIoGsO2t", required = true)
    private String bookingId;

    @Schema(description = "status", example = "complete", required = true)
    String status;

    @Schema(description = "createdAt", example = "2024-02-23 09:14:50.567400", required = true)
    private Instant createdAt;
}