package com.myroom.organizationpay.api.resource;

import com.myroom.organizationpay.api.constants.ApiConstants;
import com.myroom.organizationpay.api.model.AccountDetailsResponseModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Organization Account Resource")

@RestController
@RequestMapping(value = ApiConstants.ORGANIZATION_PAY_API_V1 + ApiConstants.ORGANIZATION_ACCOUNT)
public interface AccountResource {
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = ApiConstants.MESSAGE_SUCCESS),
                    @ApiResponse(responseCode = "400", description = ApiConstants.MESSAGE_BAD_REQUEST),
                    @ApiResponse(responseCode = "500", description = ApiConstants.MESSAGE_INTERNAL_SERVER_ERROR),
                    @ApiResponse(responseCode = "503", description = ApiConstants.MESSAGE_SERVICE_UNAVAILABLE)
            }
    )
    @Operation(method = "GET", summary = "Get account details by organizationId")
    @GetMapping("/{organizationId}")
    ResponseEntity<AccountDetailsResponseModel> getAccountDetailsByOrganizationId(
            @Schema(description = "organizationId", example = "asdfasdf", required = true)
            @PathVariable("organizationId") String organizationId
    );

    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = ApiConstants.MESSAGE_SUCCESS),
                    @ApiResponse(responseCode = "400", description = ApiConstants.MESSAGE_BAD_REQUEST),
                    @ApiResponse(responseCode = "500", description = ApiConstants.MESSAGE_INTERNAL_SERVER_ERROR),
                    @ApiResponse(responseCode = "503", description = ApiConstants.MESSAGE_SERVICE_UNAVAILABLE)
            }
    )
    @Operation(method = "GET", summary = "Check account is active or not")
    @GetMapping("/{organizationId}/active")
    ResponseEntity<Boolean> checkAccountActive(
            @Parameter(name = "organizationId", description = "organizationId")
            @Schema(description = "organizationId", example = "kjnasdfiubw78eawsedbfasdf", required = true)
            @PathVariable("organizationId") String organizationId
    );
}