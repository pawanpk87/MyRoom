package com.myroom.organizationpay.usecase;

import com.myroom.organizationpay.api.model.AccountDetailsResponseModel;
import com.myroom.organizationpay.data.entity.OrganizationAccount;

public interface OrganizationAccountService {
    AccountDetailsResponseModel getAccountDetails(String organizationId);

    OrganizationAccount getOrganizationAccountDetails(String organizationId);
}
