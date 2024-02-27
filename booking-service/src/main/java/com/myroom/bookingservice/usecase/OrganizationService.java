package com.myroom.bookingservice.usecase;

import com.myroom.bookingservice.data.dto.OrganizationData;

public interface OrganizationService {
    OrganizationData getOrganization(String organizationId);

    boolean isExists(String organizationId);
}
