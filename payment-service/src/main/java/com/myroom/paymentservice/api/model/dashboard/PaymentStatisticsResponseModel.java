package com.myroom.paymentservice.api.model.dashboard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentStatisticsResponseModel {
    @Schema(description = "totalRevenue", example = "1200")
    private BigDecimal totalRevenue;

    @Schema(description = "total", example = "120")
    private BigDecimal total;

    @Schema(description = "data", example = "[300, 1800, 700, 30000, 19000, 34000, 1000]")
    private List<Long> data;
}
