package com.myroom.organizationpay.controller;

import com.myroom.organizationpay.api.model.AccountOnboardingRequestModel;
import com.myroom.organizationpay.api.model.AccountOnboardingResponseModel;
import com.myroom.organizationpay.api.resource.AccountOnboardingResource;
import com.myroom.organizationpay.data.dto.CreateStripeAccountLinkResponseDto;
import com.myroom.organizationpay.usecase.AccountOnboardingService;
import com.myroom.organizationpay.usecase.StripeService;
import com.stripe.param.AccountLinkCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@Slf4j
public class AccountOnboardingController implements AccountOnboardingResource {
    @Autowired
    AccountOnboardingService accountOnboardingService;

    @Autowired
    StripeService stripeService;


    @Override
    public ResponseEntity<AccountOnboardingResponseModel> accountOnboarding(String organizationId, AccountOnboardingRequestModel accountOnboardingRequestModel) {
        log.info("Received request for account onboarding");
        log.info("Onboarding Organization Account for organizationId: {}", organizationId);
        AccountOnboardingResponseModel accountOnboardingResponseModel = accountOnboardingService.onboard(organizationId, accountOnboardingRequestModel);
        log.info("Onboard Organization Account response: {}", accountOnboardingResponseModel);
        //var stripeAccountOnboardingUrl = accountOnboardingResponseModel.getLink().getUrl();
        return  new ResponseEntity<>(accountOnboardingResponseModel, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> getAccountLink(String accountId) {
        log.info("Received request for new account_link for accountId: {}", accountId);
        CreateStripeAccountLinkResponseDto linkResponseDto = stripeService.createStripeAccountLinkByStripeAccountId(accountId, AccountLinkCreateParams.Type.ACCOUNT_ONBOARDING);
        log.info("Created new account_link: {}", linkResponseDto);
        var stripeAccountOnboardingUrl = linkResponseDto.getUrl();
        return new ResponseEntity<>(stripeAccountOnboardingUrl, HttpStatus.CREATED);
    }

    @Override
    public void verifyAccountOnboarding(String accountId) {
        log.info("Received request verify account onboarding for accountId: {}", accountId);
        accountOnboardingService.verifyAccountOnboardingByAccountId(accountId);
    }
}
