package com.myroom.organizationpay.api.resource;

import com.myroom.organizationpay.api.constants.ApiConstants;
import com.myroom.organizationpay.api.model.AccountOnboardingRequestModel;
import com.myroom.organizationpay.api.model.AccountOnboardingResponseModel;
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

@Tag(name = "Account Onboarding")

@RestController
@RequestMapping(value = ApiConstants.ORGANIZATION_PAY_API_V1 + ApiConstants.ORGANIZATION_ACCOUNT_ONBOARDING)
public interface AccountOnboardingResource {
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = ApiConstants.MESSAGE_SUCCESS),
                    @ApiResponse(responseCode = "400", description = ApiConstants.MESSAGE_BAD_REQUEST),
                    @ApiResponse(responseCode = "500", description = ApiConstants.MESSAGE_INTERNAL_SERVER_ERROR),
                    @ApiResponse(responseCode = "503", description = ApiConstants.MESSAGE_SERVICE_UNAVAILABLE)
            }
    )
    @Operation(method = "POST", summary = "Onboard Organization Account(Stripe)")
    @PostMapping("/{organizationId}")
    ResponseEntity<AccountOnboardingResponseModel> accountOnboarding(
            @Parameter(name = "organizationId", description = "organization id")
            @Schema(description = "organizationId", example = "org_asdfkjnasd", required = true)
            @PathVariable String organizationId,
            @RequestBody @Valid AccountOnboardingRequestModel accountOnboardingRequestModel
    );

    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = ApiConstants.MESSAGE_SUCCESS),
                    @ApiResponse(responseCode = "400", description = ApiConstants.MESSAGE_BAD_REQUEST),
                    @ApiResponse(responseCode = "500", description = ApiConstants.MESSAGE_INTERNAL_SERVER_ERROR),
                    @ApiResponse(responseCode = "503", description = ApiConstants.MESSAGE_SERVICE_UNAVAILABLE)
            }
    )
    @Operation(method = "GET", summary = "Get account link for account_onboarding/account_update")
    @GetMapping
    ResponseEntity<String> getAccountLink(
            @Parameter(name = "accountId", description = "stripe account id")
            @Schema(description = "organizationId", example = "acct_1Mt0CORHFI4mz9Rw", required = true)
            @RequestParam(name = "accountId", required = true) String accountId
    );

    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = ApiConstants.MESSAGE_SUCCESS),
                    @ApiResponse(responseCode = "400", description = ApiConstants.MESSAGE_BAD_REQUEST),
                    @ApiResponse(responseCode = "500", description = ApiConstants.MESSAGE_INTERNAL_SERVER_ERROR),
                    @ApiResponse(responseCode = "503", description = ApiConstants.MESSAGE_SERVICE_UNAVAILABLE)
            }
    )
    @Operation(method = "GET", summary = "Verify the account onboarding")
    @GetMapping("/verify")
    void verifyAccountOnboarding(
            @Parameter(name = "accountId", description = "stripe account id")
            @Schema(description = "organizationId", example = "acct_1Mt0CORHFI4mz9Rw", required = true)
            @RequestParam(name = "accountId", required = true) String accountId
    );
}