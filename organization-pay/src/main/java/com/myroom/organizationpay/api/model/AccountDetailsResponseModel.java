package com.myroom.organizationpay.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.myroom.organizationpay.api.constants.OrganizationAccountStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountDetailsResponseModel {
    @Schema(description = "id", example = "0a0b86a0-5101-44dd-832c-10d2834fb6e7", required = true)
    private String id;

    @Schema(description = "organizationId", example = "org_kjansdf8u3wk976", required = true)
    private String organizationId;

    @Schema(description = "status", example = "ACTIVE", required = true)
    private OrganizationAccountStatus status;

    @Schema(description = "Stripe Account Details",  required = true)
    private OrganizationStripeAccountDetailsModel stripeAccountDetails;

    @Schema(description = "createdAt", example = "date", required = true)
    private Instant createdAt;

    @Schema(description = "status", example = "date", required = true)
    private Instant updatedAt;
}