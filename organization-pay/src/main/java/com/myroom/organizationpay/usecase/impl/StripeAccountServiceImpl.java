package com.myroom.organizationpay.usecase.impl;

import com.myroom.organizationpay.api.constants.ApiConstants;
import com.myroom.organizationpay.api.constants.stripe.StripeAccountStatus;
import com.myroom.organizationpay.data.entity.StripeAccount;
import com.myroom.organizationpay.exception.OrganizationPayServiceRuntimeException;
import com.myroom.organizationpay.repository.StripeAccountRepository;
import com.myroom.organizationpay.usecase.StripeAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class StripeAccountServiceImpl implements StripeAccountService {
    @Autowired
    StripeAccountRepository stripeAccountRepository;


    @Override
    public Boolean isAccountExists(String organizationId) {
        return null;
    }

    @Override
    public StripeAccount getAccountDetails(String organizationId) {
        return null;
    }

    @Override
    public StripeAccount getAccountDetailsByAccountId(String accountId) {
        log.info("Fetching stripeAccount details by accountId");
        try {
            Optional<StripeAccount> stripeAccountOpt = stripeAccountRepository.findByAccountId(accountId);

            if(stripeAccountOpt.isEmpty()){
                log.info("Couldn't find any StripeAccountDetails with this accountId: {}", accountId);
                throw new OrganizationPayServiceRuntimeException(ApiConstants.ORGANIZATION_ACCOUNT_NOT_FOUND);
            }

            log.info("Fetched stripeAccount: {}", stripeAccountOpt.get());
            return stripeAccountOpt.get();
        }catch (Exception ex){
            throw new OrganizationPayServiceRuntimeException(ApiConstants.MESSAGE_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void updateStatusByOrganizationId(String organizationId, StripeAccountStatus stripeAccountStatus) {
        log.info("Updating stripe account status to: {}", stripeAccountStatus);

        try{
            stripeAccountRepository.updateStatusByOrganizationId(organizationId, stripeAccountStatus);

            log.info("Updated stripe account status successfully");
        }catch (Exception ex){
            log.error("Failed to stripe account, error: {}", ex.getMessage());
            throw new OrganizationPayServiceRuntimeException(ApiConstants.MESSAGE_INTERNAL_SERVER_ERROR);
        }
    }
}