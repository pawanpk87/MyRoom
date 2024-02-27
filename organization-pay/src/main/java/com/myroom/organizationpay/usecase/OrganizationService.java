package com.myroom.organizationpay.usecase;

import com.myroom.organizationpay.data.dto.OrganizationResponseDto;

public interface OrganizationService {
    OrganizationResponseDto getOrganization(String organizationId);

    boolean verifyCurrentUser(String organizationId, String uid);
}
