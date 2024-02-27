package com.myroom.onboardingservice.usecase;

import com.myroom.onboardingservice.data.dto.AccountOnboardingResponseDto;
import com.myroom.onboardingservice.data.dto.OrganizationAccountOnboardingRequestModelDto;

public interface OrganizationPayService {
    AccountOnboardingResponseDto onboardOrganizationAccount(String organizationId, OrganizationAccountOnboardingRequestModelDto organizationAccountOnboardingRequestModelDto);
}
