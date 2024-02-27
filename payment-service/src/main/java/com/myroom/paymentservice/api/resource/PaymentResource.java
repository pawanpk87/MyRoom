package com.myroom.paymentservice.api.resource;

import com.myroom.paymentservice.api.constants.ApiConstants;
import com.myroom.paymentservice.api.model.PaymentDetailsResponseModel;
import com.myroom.paymentservice.api.model.PaymentOrderRequestModel;
import com.myroom.paymentservice.api.model.PaymentOrderResponseModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@Tag(name = "Payment Order")

@RestController
@RequestMapping(value = ApiConstants.PAYMENT_SERVICE_API_V1 + ApiConstants.PAYMENT_ORDER)
public interface PaymentResource {
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = ApiConstants.MESSAGE_SUCCESS),
                    @ApiResponse(responseCode = "400", description = ApiConstants.MESSAGE_BAD_REQUEST),
                    @ApiResponse(responseCode = "500", description = ApiConstants.MESSAGE_INTERNAL_SERVER_ERROR),
                    @ApiResponse(responseCode = "503", description = ApiConstants.MESSAGE_SERVICE_UNAVAILABLE)
            }
    )
    @Operation(method = "POST", summary = "Create payment order.")
    @PostMapping
    ResponseEntity<PaymentOrderResponseModel> createPaymentOrder(
            @RequestBody @Valid PaymentOrderRequestModel paymentOrderRequestModel
    );

    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = ApiConstants.MESSAGE_SUCCESS),
                    @ApiResponse(responseCode = "400", description = ApiConstants.MESSAGE_BAD_REQUEST),
                    @ApiResponse(responseCode = "500", description = ApiConstants.MESSAGE_INTERNAL_SERVER_ERROR)
            }
    )
    @Operation(method = "POST", summary = "Get Payment detail by booking id.")
    @GetMapping("/{bookingId}")
    ResponseEntity<PaymentDetailsResponseModel> getPaymentDetailsByBookingId(
            @Parameter(name = "bookingId", description = "bookingId")
            @Schema(description = "bookingId", example = "bk_El440oZ8g9BNVIoGsO2t", required = true)
            @PathVariable("bookingId") String bookingId
    );

    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = ApiConstants.MESSAGE_SUCCESS),
                    @ApiResponse(responseCode = "400", description = ApiConstants.MESSAGE_BAD_REQUEST),
                    @ApiResponse(responseCode = "500", description = ApiConstants.MESSAGE_INTERNAL_SERVER_ERROR),
                    @ApiResponse(responseCode = "503", description = ApiConstants.MESSAGE_SERVICE_UNAVAILABLE)
            }
    )
    @Operation(method = "GET", summary = "Verify payment")
    @PostMapping("/{sessionId}")
    RedirectView handlePaymentOrderSuccess(
            @Parameter(name = "sessionId", description = "stripe sessionId id")
            @Schema(description = "sessionId", example = "cs_test_a11YYufWQzNY63zpQ6QSNRQh", required = true)
            @PathVariable("sessionId") String sessionId,
            @RequestParam(name = "type", required = true) String type
    );
}