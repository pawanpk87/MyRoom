package com.myroom.bookingservice.api.resource.dashboard;

import com.myroom.bookingservice.api.constants.ApiConstants;
import com.myroom.bookingservice.api.model.dashboard.BookingsCountStatisticsResponseModel;
import com.myroom.bookingservice.api.model.dashboard.BookingsStatisticsResponseModel;
import com.myroom.bookingservice.api.model.dashboard.PeopleCountsResponseModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@Tag(name = "Booking Dashboard(Statistics)")

@RestController
@RequestMapping(ApiConstants.BOOKING_SERVICE + ApiConstants.BOOKING_DASHBOARD + ApiConstants.BOOKING_STATISTIC)
public interface BookingStatisticDashboardResource {
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = ApiConstants.MESSAGE_SUCCESS),
                    @ApiResponse(responseCode = "400", description = ApiConstants.MESSAGE_BAD_REQUEST),
                    @ApiResponse(responseCode = "500", description = ApiConstants.MESSAGE_INTERNAL_SERVER_ERROR),
            }
    )
    @Operation(method = "GET", summary = "Get Booking statistics")
    @GetMapping
    ResponseEntity<BookingsStatisticsResponseModel> handleGetBookingStatistics(
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
    @Operation(method = "GET", summary = "Get Booking statistics")
    @GetMapping("/count")
    ResponseEntity<BookingsCountStatisticsResponseModel> handleGetAllBookings(
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
    @Operation(method = "GET", summary = "Get People Counts statistics")
    @GetMapping("/people-counts")
    ResponseEntity<PeopleCountsResponseModel> handleGetPeopleCounts(
            @Parameter(name = "organizationId", description = "start date")
            @Schema(description = "Reference", example = "organizationId", required = true)
            @RequestParam String organizationId
    );
}
