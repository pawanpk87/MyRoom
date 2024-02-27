package com.myroom.bookingservice.api.model.query;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.myroom.bookingservice.api.constants.BookingStatus;
import com.myroom.bookingservice.api.model.query.BookingDateRequestModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetAllBookingsQuery {
    @Schema(description = "id", example = "bk_akjn7883nasd", required = false)
    String id;

    @Schema(description = "uid", example = "IbW3MtSX6GM35xaYk8PV4HxsEP72", required = false)
    String uid;

    @Schema(description = "bookingDate", example = "{\n" +
            "    \"operator\": \"EQUAL\",\n" +
            "    \"bookingDate\": \"2024-01-28T16:53:15.708706Z\"\n" +
            "}", required = false)
    BookingDateRequestModel bookingDate;

    @Schema(description = "checkInDate", example = "{\n" +
            "    \"operator\": \"EQUAL\",\n" +
            "    \"checkInDate\": \"2024-01-28T16:53:15.708706Z\"\n" +
            "}", required = false)
    CheckInDateRequestModel checkInDate;

    @Schema(description = "checkOutDate", example = "{\n" +
            "    \"operator\": \"EQUAL\",\n" +
            "    \"checkOutDate\": \"2024-01-28T16:53:15.708706Z\"\n" +
            "}", required = false)
    CheckOutDateRequestModel checkOutDate;

    @Schema(description = "amount", example = "{\n" +
            "    \"operator\": \"EQUAL\",\n" +
            "    \"amount\": \"1200\"\n" +
            "}", required = false)
    AmountRequestModel amount;

    @Schema(description = "booking status", example = "CONFIRMED", required = true)
    BookingStatus status;

    @Schema(description = "page", example = "1", required = false)
    Integer page;

    @Schema(description = "size", example = "20", required = false)
    Integer size;
}
