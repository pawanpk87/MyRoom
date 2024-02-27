package com.myroom.bookingservice.api.resource;

import com.myroom.bookingservice.api.constants.ApiConstants;
import com.myroom.bookingservice.api.model.*;
import com.myroom.bookingservice.api.model.query.GetAllBookingsQuery;
import com.myroom.bookingservice.data.entity.BookingDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;

@Tag(name = "Booking Service")

@RestController
@RequestMapping(ApiConstants.BOOKING_SERVICE)
public interface BookingResource {
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = ApiConstants.MESSAGE_SUCCESS),
                    @ApiResponse(responseCode = "400", description = ApiConstants.MESSAGE_BAD_REQUEST),
                    @ApiResponse(responseCode = "500", description = ApiConstants.MESSAGE_INTERNAL_SERVER_ERROR),
            }
    )
    @Operation(method = "POST", summary = "Create Booking")
    @PostMapping("/{bookingRequestId}")
    ResponseEntity<BookingOrderResponseModel> createBooking(
            @Parameter(name = "bookingRequestId", description = "booking request id")
            @Schema(description = "Reference", example = "bk_req_8732njsf87yh", required = true)
            @PathVariable String bookingRequestId,
            @RequestBody @Valid BookingOrderRequestModel bookingOrderRequestModel);

    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = ApiConstants.MESSAGE_SUCCESS),
                    @ApiResponse(responseCode = "400", description = ApiConstants.MESSAGE_BAD_REQUEST),
                    @ApiResponse(responseCode = "500", description = ApiConstants.MESSAGE_INTERNAL_SERVER_ERROR),
            }
    )
    @Operation(method = "POST", summary = "Update Booking")
    @PatchMapping("/{bookingId}")
    ResponseEntity<BookingDataResponseModel> updateBooking(
            @Parameter(name = "bookingId", description = "bookingId")
            @Schema(description = "Reference", example = "bk_8732njasdfasf87yh", required = true)
            @PathVariable String bookingId,
            @RequestBody @Valid UpdateBookingOrderRequestModel updateBookingOrderRequestModel);

    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = ApiConstants.MESSAGE_SUCCESS),
                    @ApiResponse(responseCode = "400", description = ApiConstants.MESSAGE_BAD_REQUEST),
                    @ApiResponse(responseCode = "500", description = ApiConstants.MESSAGE_INTERNAL_SERVER_ERROR),
            }
    )
    @Operation(method = "POST", summary = "Get Bookings")
    @PostMapping("/search")
    ResponseEntity<Page<BookingDetails>> getBookings(
            @RequestBody GetAllBookingsQuery query
    );

    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = ApiConstants.MESSAGE_SUCCESS),
                    @ApiResponse(responseCode = "400", description = ApiConstants.MESSAGE_BAD_REQUEST),
                    @ApiResponse(responseCode = "500", description = ApiConstants.MESSAGE_INTERNAL_SERVER_ERROR),
            }
    )
    @Operation(method = "GET", summary = "Get Booking Data By Booking Request Id")
    @GetMapping
    ResponseEntity<BookingDataResponseModel> getBookingDataByBookingRequestId(
            @Parameter(name = "bookingRequestId", description = "booking request id")
            @Schema(description = "Reference", example = "bk_req_8732njsf87yh", required = true)
            @RequestParam(value = "bookingRequestId", required = true) String bookingRequestId);

    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = ApiConstants.MESSAGE_SUCCESS),
                    @ApiResponse(responseCode = "400", description = ApiConstants.MESSAGE_BAD_REQUEST),
                    @ApiResponse(responseCode = "500", description = ApiConstants.MESSAGE_INTERNAL_SERVER_ERROR),
            }
    )
    @Operation(method = "GET", summary = "Get Booking Data By Id")
    @GetMapping("/{bookingId}")
    ResponseEntity<BookingDataResponseModel> getBookingData(
            @Parameter(name = "bookingId", description = "booking Id")
            @Schema(description = "Reference", example = "bk_8732njsf87yh", required = true)
            @PathVariable String bookingId);

    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = ApiConstants.MESSAGE_SUCCESS),
                    @ApiResponse(responseCode = "400", description = ApiConstants.MESSAGE_BAD_REQUEST),
                    @ApiResponse(responseCode = "500", description = ApiConstants.MESSAGE_INTERNAL_SERVER_ERROR),
            }
    )
    @Operation(method = "GET", summary = "Called after payment is completed")
    @GetMapping("/{bookingId}/payment/success")
    RedirectView handleBookingPaymentSuccess(
            @Parameter(name = "bookingId", description = "booking Id")
            @Schema(description = "Reference", example = "bk_8732njsf87yh", required = true)
            @PathVariable String bookingId);

    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = ApiConstants.MESSAGE_SUCCESS),
                    @ApiResponse(responseCode = "400", description = ApiConstants.MESSAGE_BAD_REQUEST),
                    @ApiResponse(responseCode = "500", description = ApiConstants.MESSAGE_INTERNAL_SERVER_ERROR),
            }
    )
    @Operation(method = "GET", summary = "Called after payment is canceled")
    @GetMapping("/{bookingId}/payment/cancel")
    RedirectView handleBookingPaymentCancel(
            @Parameter(name = "bookingId", description = "booking Id")
            @Schema(description = "Reference", example = "bk_8732njsf87yh", required = true)
            @PathVariable String bookingId);
}