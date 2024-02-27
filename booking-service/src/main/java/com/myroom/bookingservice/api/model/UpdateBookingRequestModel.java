package com.myroom.bookingservice.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.myroom.bookingservice.api.constants.Guest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateBookingRequestModel {
    @Schema(required = false)
    String bookingRequestId;

    @Schema(description = "check-in", example = "2023-12-24T06:21:38.699Z")
    String checkIn;

    @Schema(description = "check-out", example = "2023-12-24T06:21:38.699Z")
    String checkOut;

    @Schema(description = "guests")
    Guest guests;

    @Schema(required = false)
    String roomId;

    @Schema(description = "user id", example = "901237412412", required = true)
    String uid;
}