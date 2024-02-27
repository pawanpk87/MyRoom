package com.myroom.bookingservice.api.constants;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.myroom.bookingservice.api.model.BookingRequestRequestModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingRequest {
    @Schema(description = "bookingRequestId", example = "bk_req_235235", required = true)
    String bookingRequestId;

    @Schema(description = "booking request date", example = "2023-12-24T12:30:45Z", required = true)
    String bookingRequestDate;

    @Schema(description = "booking request status", example = "CONFIRMED", required = true)
    BookingRequestStatus status;

    @Schema(description = "payment type", example = "ONLINE_PAYMENT", required = true)
    PaymentType paymentType;

    @Schema(description = "payment method type")
    PaymentMethodType paymentMethodType;

    @Schema(description = "booking detail", required = true)
    BookingRequestRequestModel details;
}