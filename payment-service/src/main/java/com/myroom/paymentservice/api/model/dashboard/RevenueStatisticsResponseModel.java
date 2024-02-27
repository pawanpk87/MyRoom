package com.myroom.paymentservice.api.model.dashboard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RevenueStatisticsResponseModel {
    @Schema(description = "current", example = "1200")
    List<Long> curr;

    @Schema(description = "prev", example = "1200")
    List<Long> prev;
}
