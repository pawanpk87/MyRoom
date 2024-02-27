package com.myroom.organizationpay.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.myroom.organizationpay.api.constants.OrganizationAccountStatus;
import com.myroom.organizationpay.data.dto.CreateStripeAccountLinkResponseDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountOnboardingResponseModel {
    private OrganizationAccountStatus status;

    private CreateStripeAccountLinkResponseDto link;
}