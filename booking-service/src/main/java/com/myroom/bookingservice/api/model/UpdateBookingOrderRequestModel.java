package com.myroom.bookingservice.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.myroom.bookingservice.api.constants.BookingStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateBookingOrderRequestModel {
    @Schema(description = "bookingId", example = "bk_asdfas23asdgf5235")
    String bookingId;

    @Schema(description = "booking status", example = "CONFIRMED", required = true)
    BookingStatus status;
}