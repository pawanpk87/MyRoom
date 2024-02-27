package com.myroom.onboardingservice.api.resource;

import com.myroom.onboardingservice.api.constants.ApiConstants;
import com.myroom.onboardingservice.api.model.OrganizationAccountOnboardingRequestModel;
import com.myroom.onboardingservice.api.model.OrganizationAccountOnboardingResponseModel;
import com.myroom.onboardingservice.api.model.OrganizationOnboardingRequestModel;
import com.myroom.onboardingservice.api.model.OrganizationOnboardingResponseModel;
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

@Tag(name = "Organization Onboarding Resource")

@RestController
@RequestMapping(value = ApiConstants.ONBOARDING_SERVICE + ApiConstants.ORGANIZATION_ONBOARDING)
public interface OrganizationOnboardingResource {
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = ApiConstants.MESSAGE_SUCCESS),
                    @ApiResponse(responseCode = "400", description = ApiConstants.MESSAGE_BAD_REQUEST),
                    @ApiResponse(responseCode = "500", description = ApiConstants.MESSAGE_INTERNAL_SERVER_ERROR),
                    @ApiResponse(responseCode = "503", description = ApiConstants.MESSAGE_SERVICE_UNAVAILABLE)
            }
    )
    @Operation(method = "POST", summary = "Onboard Organization")
    @PostMapping
    ResponseEntity<OrganizationOnboardingResponseModel> onboardOrganization(@RequestBody @Valid OrganizationOnboardingRequestModel onboardingRequestModel);

    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = ApiConstants.MESSAGE_SUCCESS),
                    @ApiResponse(responseCode = "400", description = ApiConstants.MESSAGE_BAD_REQUEST),
                    @ApiResponse(responseCode = "500", description = ApiConstants.MESSAGE_INTERNAL_SERVER_ERROR),
                    @ApiResponse(responseCode = "503", description = ApiConstants.MESSAGE_SERVICE_UNAVAILABLE)
            }
    )
    @Operation(method = "POST", summary = "Onboard Organization Account")
    @PostMapping("/{organizationId}/account")
    ResponseEntity<OrganizationAccountOnboardingResponseModel> onboardOrganizationBankAccount(
            @Parameter(name = "organizationId", description = "Organization Id")
            @Schema(description = "organizationId", example = "658d6e2f9583cda663107426", required = true)
            @PathVariable String organizationId,
            @RequestBody @Valid OrganizationAccountOnboardingRequestModel organizationAccountOnboardingRequestModel);
}