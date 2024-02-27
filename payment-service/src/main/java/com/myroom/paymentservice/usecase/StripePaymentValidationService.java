package com.myroom.paymentservice.usecase;

public interface StripePaymentValidationService {
    Boolean validateOrganizationAccountActive(String organizationId);
}
