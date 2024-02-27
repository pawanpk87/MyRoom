package com.myroom.bookingservice.api.model.dashboard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.myroom.bookingservice.api.model.BookingDataResponseModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingsRecordsResponseModel {
    @Schema(description = "total", example = "120")
    private Integer total;

    @Schema(description = "data", example = "[{booking details}]")
    private List<BookingDataResponseModel> data;
}
