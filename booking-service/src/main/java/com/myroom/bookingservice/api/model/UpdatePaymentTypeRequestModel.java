package com.myroom.bookingservice.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.myroom.bookingservice.api.constants.PaymentType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdatePaymentTypeRequestModel {
    @Schema(required = false)
    String bookingRequestId;

    @Schema(description = "user id", example = "901237412412", required = true)
    String uid;

    @Schema(description = "payment type", example = "ONLINE_PAYMENT", required = true)
    PaymentType paymentType;
}
