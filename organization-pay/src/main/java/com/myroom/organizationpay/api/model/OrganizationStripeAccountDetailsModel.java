package com.myroom.organizationpay.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.myroom.organizationpay.api.constants.stripe.StripeAccountStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrganizationStripeAccountDetailsModel {
    @Schema(description = "accountId", example = "org_kjansdf8u3wk976", required = true)
    private String accountId;

    @Schema(description = "stripe account status", example = "ACCOUNT_ONBOARDING_PENDING", required = true)
    private StripeAccountStatus status;
}
