package com.myroom.organizationpay.data.mapper;

import com.myroom.organizationpay.api.model.AccountOnboardingResponseModel;
import com.myroom.organizationpay.data.dto.CreateStripeAccountLinkResponseDto;
import com.myroom.organizationpay.data.entity.OrganizationAccount;
import org.springframework.stereotype.Component;

@Component
public class OrganizationAccountOnboardingMapper {
    public AccountOnboardingResponseModel toAccountOnboardingResponseModel(OrganizationAccount organizationAccount, CreateStripeAccountLinkResponseDto stripeAccountLink) {
        return AccountOnboardingResponseModel.builder()
                .status(organizationAccount.getStatus())
                .link(stripeAccountLink)
                .build();
    }

    public AccountOnboardingResponseModel toAccountOnboardingResponseModel(CreateStripeAccountLinkResponseDto stripeAccountLink) {
        return AccountOnboardingResponseModel.builder()
                .link(stripeAccountLink)
                .build();
    }
}