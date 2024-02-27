package com.myroom.organizationpay.usecase.impl;

import com.myroom.organizationpay.api.constants.ApiConstants;
import com.myroom.organizationpay.api.constants.OrganizationAccountStatus;
import com.myroom.organizationpay.api.constants.stripe.StripeAccountStatus;
import com.myroom.organizationpay.api.model.AccountOnboardingRequestModel;
import com.myroom.organizationpay.api.model.AccountOnboardingResponseModel;
import com.myroom.organizationpay.data.dto.CreateStripeAccountLinkResponseDto;
import com.myroom.organizationpay.data.entity.OrganizationAccount;
import com.myroom.organizationpay.data.mapper.OrganizationAccountOnboardingMapper;
import com.myroom.organizationpay.exception.OrganizationPayServiceRuntimeException;
import com.myroom.organizationpay.exception.OrganizationPayUnAuthorizedAccess;
import com.myroom.organizationpay.repository.OrganizationAccountRepository;
import com.myroom.organizationpay.repository.StripeAccountRepository;
import com.myroom.organizationpay.usecase.*;
import com.stripe.param.AccountLinkCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AccountOnboardingServiceImpl implements AccountOnboardingService {
    @Autowired
    AccountService accountService;

    @Autowired
    StripeAccountService stripeAccountService;

    @Autowired
    OrganizationService organizationService;

    @Autowired
    StripeService stripeService;

    @Autowired
    OrganizationAccountOnboardingMapper organizationAccountOnboardingMapper;

    @Autowired
    OrganizationAccountRepository organizationAccountRepository;

    @Autowired
    StripeAccountRepository stripeAccountRepository;


    @Override
    public AccountOnboardingResponseModel onboard(String organizationId, AccountOnboardingRequestModel accountOnboardingRequestModel) {
        log.info("Onboarding Organization Account : {}", accountOnboardingRequestModel);

        if(!organizationService.verifyCurrentUser(organizationId, accountOnboardingRequestModel.getUid())){
            throw new OrganizationPayUnAuthorizedAccess(ApiConstants.UNAUTHORIZED_USER, "User does not have the required permissions.", "Invalid adminId");
        }

        AccountOnboardingResponseModel accountOnboardingResponseModel = null;

        if(accountService.isAccountExists(organizationId)){
            accountOnboardingResponseModel = handleOrganizationAccountOnboarding(organizationId);
        }else{
            accountOnboardingResponseModel = createAccount(organizationId);
        }

        return accountOnboardingResponseModel;
    }

    @Override
    public void verifyAccountOnboardingByAccountId(String accountId) {
        // verify account onboarding and change the account status to ACTIVE
    }

    private AccountOnboardingResponseModel handleOrganizationAccountOnboarding(String organizationId) {
        log.info("Handling organization account onboarding for organizationId: {}", organizationId);

        log.info("Fetching organization account status");
        OrganizationAccountStatus organizationAccountStatus = organizationAccountRepository.findStatusByOrganizationId(organizationId);
        log.info("Fetched organization account status: {}", organizationAccountStatus);

        switch (organizationAccountStatus){
            case INACTIVE:
                log.info("Organization account is inactive. Initiating Stripe account onboarding.");
                return handleStipeAccountOnboarding(organizationId);

            case ACTIVE:
                log.error("Organization account is already active.");
                throw new OrganizationPayServiceRuntimeException("Organization account is already active");

            default:
                log.error("Invalid organization status: {}", organizationAccountStatus);
                throw new OrganizationPayServiceRuntimeException(ApiConstants.MESSAGE_INTERNAL_SERVER_ERROR);
        }
    }

    private AccountOnboardingResponseModel handleStipeAccountOnboarding(String organizationId) {
        log.info("Handling Stripe account onboarding for organizationId: {}", organizationId);

        log.info("Fetching organization's stripe account status");
        StripeAccountStatus stripeAccountStatus = stripeAccountRepository.findStatusByOrganizationId(organizationId);
        log.info("Fetched organization's stripe account status: {}", stripeAccountStatus);

        switch (stripeAccountStatus){
            case ACCOUNT_ONBOARDING_PENDING, INACTIVE:
                log.info("Organization organization's stripe status is: {}", stripeAccountStatus);
                CreateStripeAccountLinkResponseDto stripeAccountLink = stripeService.createStripeAccountLinkByStripeOrganizationId(organizationId, AccountLinkCreateParams.Type.ACCOUNT_ONBOARDING);
                return organizationAccountOnboardingMapper.toAccountOnboardingResponseModel(stripeAccountLink);

            case ACTIVE:
                log.error("Organization's stripe account is already active");
                // Update the organization account status
                accountService.updateStatusByOrganizationId(organizationId, OrganizationAccountStatus.ACTIVE);
                throw new OrganizationPayServiceRuntimeException("Organization account is already active");

            default:
                log.error("Invalid organization status: {}", stripeAccountStatus);
                throw new OrganizationPayServiceRuntimeException(ApiConstants.MESSAGE_INTERNAL_SERVER_ERROR);
        }
    }

    private AccountOnboardingResponseModel createAccount(String organizationId) {
        // Step1: Create Organization Account
        OrganizationAccount organizationAccount = accountService.createAccount(organizationId);

        // Step1: Get Stripe account_link
        CreateStripeAccountLinkResponseDto stripeAccountLink = stripeService.createStripeAccountLinkByStripeOrganizationId(organizationId, AccountLinkCreateParams.Type.ACCOUNT_ONBOARDING);

        // Step3: Map the results
        AccountOnboardingResponseModel accountOnboardingResponseModel = organizationAccountOnboardingMapper.toAccountOnboardingResponseModel(organizationAccount, stripeAccountLink);
        return accountOnboardingResponseModel;
    }
}