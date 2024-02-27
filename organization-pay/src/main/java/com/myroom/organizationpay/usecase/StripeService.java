package com.myroom.organizationpay.usecase;

import com.myroom.organizationpay.data.dto.CreateStripeAccountLinkResponseDto;
import com.myroom.organizationpay.data.dto.StripeAccountDto;
import com.stripe.param.AccountLinkCreateParams;

import java.util.Map;


public interface StripeService {
    StripeAccountDto createStripeAccount(String organizationId);

    StripeAccountDto getAccount(String accountId);

    CreateStripeAccountLinkResponseDto createStripeAccountLinkByStripeAccountId(String accountId, AccountLinkCreateParams.Type type);

    CreateStripeAccountLinkResponseDto createStripeAccountLinkByStripeOrganizationId(String organizationId, AccountLinkCreateParams.Type type);

    void handleEvent(String payload, Map<String, String> headers);
}
