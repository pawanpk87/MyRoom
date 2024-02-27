package com.myroom.bookingservice.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.myroom.bookingservice.api.constants.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingRequestResponseModel {
    @Schema(description = "booking request id", example = "bk_req_kjhabsd78w3jba78s", required = true)
    String id;

    @Schema(description = "room id", example = "room_kjansdf8u3wk976", required = true)
    String roomId;

    @Schema(description = "check-in", example = "2023-12-24T06:21:38.699Z", required = true)
    String checkIn;

    @Schema(description = "check-out", example = "2023-12-24T06:21:38.699Z", required = true)
    String checkOut;

    @Schema(description = "guests", required = true)
    Guest guests;

    @Schema(description = "contact details")
    ContactDetails contactDetails;

    @Schema(description = "booking request status", example = "PENDING", required = true)
    BookingRequestStatus status;

    @Schema(description = "payment type", example = "ONLINE_PAYMENT", required = true)
    PaymentType paymentType;

    @Schema(description = "payment method type", example = "STRIPE", required = true)
    PaymentMethodType paymentMethodType;

    @Schema(description = "booking request date", example = "date", required = true)
    Instant bookingRequestDate;
}