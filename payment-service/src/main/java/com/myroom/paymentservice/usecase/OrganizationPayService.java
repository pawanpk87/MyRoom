package com.myroom.paymentservice.usecase;


public interface OrganizationPayService {
    public boolean isOrganizationAccountActive(String organizationId);

    String getOrganizationStripeAccountId(String organizationId);
}
