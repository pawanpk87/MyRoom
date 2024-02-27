package com.myroom.organizationpay.data.mapper;

import com.myroom.organizationpay.api.model.AccountDetailsResponseModel;
import com.myroom.organizationpay.api.model.OrganizationStripeAccountDetailsModel;
import com.myroom.organizationpay.data.entity.OrganizationAccount;
import org.springframework.stereotype.Component;


@Component
public class OrganizationAccountMapper {
    public AccountDetailsResponseModel toAccountDetailsResponseModel(OrganizationAccount organizationAccount) {
        OrganizationStripeAccountDetailsModel organizationStripeAccountDetailsModel =
                OrganizationStripeAccountDetailsModel.builder()
                        .accountId(organizationAccount.getStripeAccountRef().getAccountId())
                        .status(organizationAccount.getStripeAccountRef().getStatus())
                        .build();
        return AccountDetailsResponseModel.builder()
                .id(organizationAccount.getId())
                .organizationId(organizationAccount.getOrganizationRef())
                .status(organizationAccount.getStatus())
                .stripeAccountDetails(organizationStripeAccountDetailsModel)
                .createdAt(organizationAccount.getCreatedAt())
                .updatedAt(organizationAccount.getUpdatedAt())
                .build();
    }
}