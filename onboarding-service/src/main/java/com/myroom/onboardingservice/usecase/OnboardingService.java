package com.myroom.onboardingservice.usecase;

import com.myroom.onboardingservice.api.model.OrganizationAccountOnboardingRequestModel;
import com.myroom.onboardingservice.api.model.OrganizationOnboardingRequestModel;
import com.myroom.onboardingservice.api.model.OrganizationOnboardingResponseModel;
import com.myroom.onboardingservice.data.dto.AccountOnboardingResponseDto;
import com.myroom.onboardingservice.data.dto.StripeAccountLinkResponseDto;

public interface OnboardingService {
    OrganizationOnboardingResponseModel onboard(OrganizationOnboardingRequestModel onboardingRequestModel);

    AccountOnboardingResponseDto onboardAccount(String organizationId, OrganizationAccountOnboardingRequestModel organizationBankAccountOnboardingRequestModel);
}
