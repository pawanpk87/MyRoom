package com.myroom.organizationpay.usecase;

import com.myroom.organizationpay.api.model.AccountOnboardingRequestModel;
import com.myroom.organizationpay.api.model.AccountOnboardingResponseModel;

public interface AccountOnboardingService {
    AccountOnboardingResponseModel onboard(String organizationId, AccountOnboardingRequestModel accountOnboardingRequestModel);

    void verifyAccountOnboardingByAccountId(String accountId);
}
