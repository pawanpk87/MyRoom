package com.myroom.paymentservice.api.resource.dashboard;

import com.myroom.paymentservice.api.constants.ApiConstants;
import com.myroom.paymentservice.api.model.dashboard.PaymentStatisticsResponseModel;
import com.myroom.paymentservice.api.model.dashboard.RevenueStatisticsResponseModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Payment Order")

@RestController
@RequestMapping(value = ApiConstants.PAYMENT_SERVICE_API_V1 + ApiConstants.PAYMENT_DASHBOARD + ApiConstants.PAYMENT_STATISTIC )
public interface PaymentStatisticDashboardResource {
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = ApiConstants.MESSAGE_SUCCESS),
                    @ApiResponse(responseCode = "400", description = ApiConstants.MESSAGE_BAD_REQUEST),
                    @ApiResponse(responseCode = "500", description = ApiConstants.MESSAGE_INTERNAL_SERVER_ERROR),
            }
    )
    @Operation(method = "GET", summary = "Get Payment statistics")
    @GetMapping
    ResponseEntity<PaymentStatisticsResponseModel> handleGetBookingStatistics(
            @Parameter(name = "organizationId", description = "start date")
            @Schema(description = "Reference", example = "organizationId", required = true)
            @RequestParam String organizationId,

            @Parameter(name = "startDate", description = "start date")
            @Schema(description = "Reference", example = "date", required = true)
            @RequestParam String startDate,

            @Parameter(name = "endDate", description = "end date")
            @Schema(description = "Reference", example = "date", required = true)
            @RequestParam String endDate
    );

    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = ApiConstants.MESSAGE_SUCCESS),
                    @ApiResponse(responseCode = "400", description = ApiConstants.MESSAGE_BAD_REQUEST),
                    @ApiResponse(responseCode = "500", description = ApiConstants.MESSAGE_INTERNAL_SERVER_ERROR),
            }
    )
    @Operation(method = "GET", summary = "Get Payment statistics")
    @GetMapping("/revenue")
    ResponseEntity<RevenueStatisticsResponseModel> handleGetRevenueStatistics(
            @Parameter(name = "duration", description = "week | month | year")
            @Schema(description = "Reference", example = "week | month | year", required = true)
            @RequestParam String duration,

            @Parameter(name = "organizationId", description = "start date")
            @Schema(description = "Reference", example = "organizationId", required = true)
            @RequestParam String organizationId
    );
}
