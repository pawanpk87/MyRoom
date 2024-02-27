package com.myroom.onboardingservice.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.myroom.onboardingservice.api.constants.OrganizationAccountStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountOnboardingResponseDto {
    private OrganizationAccountStatus status;

    private StripeAccountLinkResponseDto link;
}