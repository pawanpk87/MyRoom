package com.myroom.bookingservice.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.myroom.bookingservice.api.constants.BookingStatus;
import com.myroom.bookingservice.api.constants.ContactDetails;
import com.myroom.bookingservice.api.constants.Guest;
import com.myroom.bookingservice.api.constants.PaymentType;
import com.myroom.bookingservice.data.model.BookingPaymentMetaDataModel;
import com.myroom.bookingservice.data.model.RoomMetaDataModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingDataResponseModel {
    @Schema(description = "booking id", example = "bk_98na8324lnk90aed2235")
    String id;

    @Schema(description = "bookingRequestId", example = "bk_req_235235")
    private String bookingRequestId;

    @Schema(description = "amount", example = "5000", required = true)
    private String amount;

    @Schema(description = "check-in", example = "2023-12-24T06:21:38.699Z", required = true)
    String checkIn;

    @Schema(description = "check-out", example = "2023-12-24T06:21:38.699Z", required = true)
    String checkOut;

    @Schema(description = "room details", required = true)
    RoomMetaDataModel roomDetails;

    @Schema(description = "payment meta data", required = true)
    BookingPaymentMetaDataModel paymentMetaDataModel;

    @Schema(description = "booking status", example = "CONFIRMED", required = true)
    BookingStatus status;

    @Schema(description = "guests", required = true)
    Guest guests;

    @Schema(description = "contact details", required = true)
    ContactDetails contactDetails;

    @Schema(description = "payment type", example = "ONLINE_PAYMENT", required = true)
    PaymentType paymentType;

    @Schema(description = "booking date", example = "date")
    private String bookingDate;

    @Schema(description = "user id", example = "901237412412", required = true)
    String uid;

    @Schema(description = "created at", example = "date", required = true)
    private String createdAt;

    @Schema(description = "updated at", example = "date", required = true)
    private String updatedAt;
}