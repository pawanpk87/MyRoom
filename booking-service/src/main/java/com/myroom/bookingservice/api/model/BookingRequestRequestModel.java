package com.myroom.bookingservice.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.myroom.bookingservice.api.constants.ContactDetails;
import com.myroom.bookingservice.api.constants.Guest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingRequestRequestModel {
    @Schema(description = "check-in", example = "2023-12-24T06:21:38.699Z", required = true)
    String checkIn;

    @Schema(description = "check-out", example = "2023-12-24T06:21:38.699Z", required = true)
    String checkOut;

    @Schema(description = "guests", required = true)
    Guest guests;

    @Schema(description = "room id", example = "room_kjansdf8u3wk976", required = true)
    String roomId;

    @Schema(description = "user id", example = "901237412412", required = true)
    String uid;

    @Schema(description = "contact details")
    ContactDetails contactDetails;
}