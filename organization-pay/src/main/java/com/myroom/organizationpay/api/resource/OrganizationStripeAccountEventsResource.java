package com.myroom.organizationpay.api.resource;

import com.myroom.organizationpay.api.constants.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "Organization Stripe Account Events")

@RestController
@RequestMapping(value = ApiConstants.ORGANIZATION_PAY_API_V1 + ApiConstants.ORGANIZATION_STRIPE_ACCOUNT_EVENTS)
public interface OrganizationStripeAccountEventsResource {
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = ApiConstants.MESSAGE_SUCCESS),
                    @ApiResponse(responseCode = "400", description = ApiConstants.MESSAGE_BAD_REQUEST),
                    @ApiResponse(responseCode = "500", description = ApiConstants.MESSAGE_INTERNAL_SERVER_ERROR),
                    @ApiResponse(responseCode = "503", description = ApiConstants.MESSAGE_SERVICE_UNAVAILABLE)
            }
    )
    @Operation(method = "POST", summary = "Accept account.updated events")
    @PostMapping("/account-updated")
    ResponseEntity updateAccountEvent(@RequestBody String payload, @RequestHeader Map<String, String> headers);
}