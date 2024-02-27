package com.myroom.onboardingservice.usecase;

import com.myroom.onboardingservice.data.dto.OrganizationRequestDto;
import com.myroom.onboardingservice.data.dto.OrganizationResponseDto;

public interface OrganizationService {
    OrganizationResponseDto createOrganization(OrganizationRequestDto organizationRequestDto);
}
