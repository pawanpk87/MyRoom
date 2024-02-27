package com.myroom.onboardingservice.controller;

import com.myroom.onboardingservice.api.model.OrganizationAccountOnboardingRequestModel;
import com.myroom.onboardingservice.api.model.OrganizationAccountOnboardingResponseModel;
import com.myroom.onboardingservice.api.model.OrganizationOnboardingRequestModel;
import com.myroom.onboardingservice.api.model.OrganizationOnboardingResponseModel;
import com.myroom.onboardingservice.api.resource.OrganizationOnboardingResource;
import com.myroom.onboardingservice.data.dto.AccountOnboardingResponseDto;
import com.myroom.onboardingservice.usecase.OnboardingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class OrganizationController implements OrganizationOnboardingResource {
    @Autowired
    OnboardingService onboardingService;


    @Override
    public ResponseEntity<OrganizationOnboardingResponseModel> onboardOrganization(OrganizationOnboardingRequestModel onboardingRequestModel) {
        log.info("Received Organization onboarding request");
        log.info("Onboarding organization: {}", onboardingRequestModel);
        OrganizationOnboardingResponseModel responseModel = onboardingService.onboard(onboardingRequestModel);
        log.info("Organization Onboarded successfully : {}", responseModel);
        return new ResponseEntity<>(responseModel, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<OrganizationAccountOnboardingResponseModel> onboardOrganizationBankAccount(String organizationId, OrganizationAccountOnboardingRequestModel organizationAccountOnboardingRequestModel) {
        log.info("Received Organization account onboarding request");
        log.info("Onboarding organization for organizationId: {}", organizationId);
        AccountOnboardingResponseDto accountOnboardingResponseDto = onboardingService.onboardAccount(organizationId, organizationAccountOnboardingRequestModel);
        log.info("Onboarded organization account:{}", accountOnboardingResponseDto);
        OrganizationAccountOnboardingResponseModel organizationAccountOnboardingResponseModel =
                OrganizationAccountOnboardingResponseModel.builder()
                        .link(accountOnboardingResponseDto.getLink().getUrl())
                        .build();
        return new ResponseEntity<>(organizationAccountOnboardingResponseModel, HttpStatus.OK);
    }
}