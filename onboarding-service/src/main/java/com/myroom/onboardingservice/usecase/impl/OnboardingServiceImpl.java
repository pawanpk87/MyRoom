package com.myroom.onboardingservice.usecase.impl;

import com.myroom.onboardingservice.api.constants.ApiConstants;
import com.myroom.onboardingservice.api.model.OrganizationAccountOnboardingRequestModel;
import com.myroom.onboardingservice.api.model.OrganizationOnboardingRequestModel;
import com.myroom.onboardingservice.api.model.OrganizationOnboardingResponseModel;
import com.myroom.onboardingservice.data.dto.*;
import com.myroom.onboardingservice.data.mapper.OrganizationMapper;
import com.myroom.onboardingservice.event.OrganizationOnboardingEvent;
import com.myroom.onboardingservice.exception.OnboardingServiceRuntimeException;
import com.myroom.onboardingservice.usecase.OnboardingService;
import com.myroom.onboardingservice.usecase.OrganizationPayService;
import com.myroom.onboardingservice.usecase.OrganizationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OnboardingServiceImpl implements OnboardingService {
    @Autowired
    OrganizationService organizationService;

    @Autowired
    OrganizationPayService organizationPayService;

    @Autowired
    OrganizationMapper organizationMapper;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Override
    public OrganizationOnboardingResponseModel onboard(OrganizationOnboardingRequestModel onboardingRequestModel){
        log.info("Creating organization for: {}", onboardingRequestModel);
        OrganizationRequestDto organizationRequestDto = organizationMapper.toOrganizationRequestDto(onboardingRequestModel);
        // Create organization
        OrganizationResponseDto organizationDetails = organizationService.createOrganization(organizationRequestDto);
        try{
            // Publish event for organization creation
            eventPublisher.publishEvent(
                    new OrganizationOnboardingEvent(
                            this,
                            OrganizationOnboardingEvent.EventType.ACCOUNT_CREATED,
                            organizationDetails
                    )
            );
            OrganizationOnboardingResponseModel responseModel = organizationMapper.toOrganizationOnboardingResponseModel(organizationDetails);
            return  responseModel;
        } catch (Exception ex){
            log.error("Some error curred: {}", ex.getMessage());
            throw new OnboardingServiceRuntimeException(ApiConstants.MESSAGE_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public AccountOnboardingResponseDto onboardAccount(String organizationId, OrganizationAccountOnboardingRequestModel organizationAccountOnboardingRequestModel) {
        log.info("Onboarding organization account for organizationId: {}", organizationId);
        OrganizationAccountOnboardingRequestModelDto organizationAccountOnboardingRequestModelDto =
                organizationMapper.toOrganizationAccountOnboardingRequestModelDto(organizationAccountOnboardingRequestModel);
        return organizationPayService.onboardOrganizationAccount(organizationId, organizationAccountOnboardingRequestModelDto);
    }
}