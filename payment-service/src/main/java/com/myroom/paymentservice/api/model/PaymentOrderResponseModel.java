package com.myroom.paymentservice.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentOrderResponseModel {
    @Schema(description = "stripe sessionId", example = "cs_test_a11YYufWQzNY63zpQ6QSNRQhkUpVph4WRmzW0zWJO2znZKdVujZ0N0S22u", required = true)
    private String id;

    @Schema(description = "stripe checkout url", example = "https://checkout.stripe.com/c/pay/sessionId", required = true)
    private String url;

    @Schema(description = "amount", example = "2000", required = true)
    private String amount;

    @Schema(description = "status", example = "created", required = true)
    private String status;
}