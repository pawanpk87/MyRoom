package com.myroom.bookingservice.api.model.dashboard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingsCountStatisticsResponseModel {
    @Schema(description = "checkIn", example = "12")
    private Long checkIn;

    @Schema(description = "checkOut", example = "6")
    private Long checkOut;
}
