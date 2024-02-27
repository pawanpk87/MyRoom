package com.myroom.organizationpay.usecase;

import com.myroom.organizationpay.api.constants.OrganizationAccountStatus;
import com.myroom.organizationpay.data.entity.OrganizationAccount;

public interface AccountService {
    Boolean isAccountExists(String organizationId);

    OrganizationAccount createAccount(String organizationId);

    void updateStatusByOrganizationId(String organizationId, OrganizationAccountStatus organizationAccountStatus);
}
