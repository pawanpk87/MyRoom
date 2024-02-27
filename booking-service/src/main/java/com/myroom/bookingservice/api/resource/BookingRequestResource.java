package com.myroom.bookingservice.api.resource;

import com.myroom.bookingservice.api.constants.ApiConstants;
import com.myroom.bookingservice.api.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Booking Request Resource")

@RestController
@RequestMapping(value = ApiConstants.BOOKING_SERVICE + ApiConstants.BOOKING_REQUEST)
public interface BookingRequestResource {
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = ApiConstants.MESSAGE_SUCCESS),
                    @ApiResponse(responseCode = "400", description = ApiConstants.MESSAGE_BAD_REQUEST),
                    @ApiResponse(responseCode = "500", description = ApiConstants.MESSAGE_INTERNAL_SERVER_ERROR),
            }
    )
    @Operation(method = "POST", summary = "Create Booking request")
    @PostMapping
    ResponseEntity<BookingRequestResponseModel> createBookingRequest(@RequestBody @Valid BookingRequestRequestModel bookingRequestModel);

    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = ApiConstants.MESSAGE_SUCCESS),
                    @ApiResponse(responseCode = "400", description = ApiConstants.MESSAGE_BAD_REQUEST),
                    @ApiResponse(responseCode = "500", description = ApiConstants.MESSAGE_INTERNAL_SERVER_ERROR),
                    @ApiResponse(responseCode = "404", description = ApiConstants.MESSAGE_NOT_FOUND)
            }
    )
    @Operation(method = "PATCH", summary = "Update Booking request contact details")
    @PatchMapping("/{bookingRequestId}/contact-details")
    ResponseEntity<BookingRequestResponseModel> updateContactDetails(
            @Parameter(name = "bookingRequestId", description = "booking request id")
            @Schema(description = "Reference", example = "bk_req_8732njsf87yh", required = true)
            @PathVariable String bookingRequestId,
            @RequestBody @Valid UpdateContactDetailsRequestModel updateContactDetailsRequestModel);

    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = ApiConstants.MESSAGE_SUCCESS),
                    @ApiResponse(responseCode = "400", description = ApiConstants.MESSAGE_BAD_REQUEST),
                    @ApiResponse(responseCode = "500", description = ApiConstants.MESSAGE_INTERNAL_SERVER_ERROR),
                    @ApiResponse(responseCode = "404", description = ApiConstants.MESSAGE_NOT_FOUND)
            }
    )
    @Operation(method = "PATCH", summary = "Update Booking request payment type")
    @PatchMapping("/{bookingRequestId}/payment-type")
    ResponseEntity<BookingRequestResponseModel> updatePaymentType(
            @Parameter(name = "bookingRequestId", description = "booking request id")
            @Schema(description = "Reference", example = "bk_req_8732njsf87yh", required = true)
            @PathVariable String bookingRequestId,
            @RequestBody @Valid UpdatePaymentTypeRequestModel updatePaymentTypeRequestModel);

    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = ApiConstants.MESSAGE_SUCCESS),
                    @ApiResponse(responseCode = "400", description = ApiConstants.MESSAGE_BAD_REQUEST),
                    @ApiResponse(responseCode = "500", description = ApiConstants.MESSAGE_INTERNAL_SERVER_ERROR),
                    @ApiResponse(responseCode = "404", description = ApiConstants.MESSAGE_NOT_FOUND)
            }
    )
    @Operation(method = "GET", summary = "Get Booking request by id")
    @GetMapping("/{bookingRequestId}")
    ResponseEntity<BookingRequestResponseModel> getBookingRequest(
            @Parameter(name = "bookingRequestId", description = "booking request id")
            @Schema(description = "Reference", example = "bk_req_8732njsf87yh", required = true)
            @PathVariable String bookingRequestId
    );

    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = ApiConstants.MESSAGE_SUCCESS),
                    @ApiResponse(responseCode = "400", description = ApiConstants.MESSAGE_BAD_REQUEST),
                    @ApiResponse(responseCode = "500", description = ApiConstants.MESSAGE_INTERNAL_SERVER_ERROR),
                    @ApiResponse(responseCode = "404", description = ApiConstants.MESSAGE_NOT_FOUND)
            }
    )
    @Operation(method = "PATCH", summary = "Update Booking request")
    @PatchMapping("/{bookingRequestId}")
    ResponseEntity<BookingRequestResponseModel> updateBookingRequest(
            @Parameter(name = "bookingRequestId", description = "booking request id")
            @Schema(description = "Reference", example = "bk_req_8732njsf87yh", required = true)
            @PathVariable String bookingRequestId,
            @RequestBody @Valid UpdateBookingRequestModel updateBookingRequestModel);

    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = ApiConstants.MESSAGE_SUCCESS),
                    @ApiResponse(responseCode = "400", description = ApiConstants.MESSAGE_BAD_REQUEST),
                    @ApiResponse(responseCode = "500", description = ApiConstants.MESSAGE_INTERNAL_SERVER_ERROR),
                    @ApiResponse(responseCode = "404", description = ApiConstants.MESSAGE_NOT_FOUND)
            }
    )
    @Operation(method = "GET", summary = "Cancel Booking request by id")
    @GetMapping("/{bookingRequestId}/cancel")
    ResponseEntity<CancelBookingRequestResponseModel> cancelBookingRequest(
            @Parameter(name = "bookingRequestId", description = "booking request id")
            @Schema(description = "Reference", example = "bk_req_8732njsf87yh", required = true)
            @PathVariable String bookingRequestId,
            @RequestBody @Valid CancelBookingRequestModel cancelBookingRequestModel
    );
}