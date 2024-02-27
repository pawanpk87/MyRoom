package com.myroom.paymentservice.api.resource;

import com.myroom.paymentservice.api.constants.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "Stripe Payment Event Resource (Stripe webhook event)")

@RestController
@RequestMapping(value = ApiConstants.PAYMENT_SERVICE_API_V1 + ApiConstants.STRIPE_PAYMENT_EVENTS)
public interface StripePaymentEventResource {
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = ApiConstants.MESSAGE_SUCCESS),
                    @ApiResponse(responseCode = "400", description = ApiConstants.MESSAGE_BAD_REQUEST),
                    @ApiResponse(responseCode = "500", description = ApiConstants.MESSAGE_INTERNAL_SERVER_ERROR),
                    @ApiResponse(responseCode = "503", description = ApiConstants.MESSAGE_SERVICE_UNAVAILABLE)
            }
    )
    @Operation(method = "POST", summary = "Accept checkout.session.completed event")
    @PostMapping("/payment-update")
    ResponseEntity updatePaymentEvent(@RequestBody String payload, @RequestHeader Map<String, String> headers);
}