package com.myroom.organizationpay.usecase;

import com.myroom.organizationpay.api.constants.stripe.StripeAccountStatus;
import com.myroom.organizationpay.data.entity.StripeAccount;

public interface StripeAccountService {
    Boolean isAccountExists(String organizationId);

    StripeAccount getAccountDetails(String organizationId);

    StripeAccount getAccountDetailsByAccountId(String accountId);

    void updateStatusByOrganizationId(String organizationId, StripeAccountStatus stripeAccountStatus);
}
