package com.myroom.onboardingservice.data.mapper;

import com.myroom.onboardingservice.api.model.OrganizationAccountOnboardingRequestModel;
import com.myroom.onboardingservice.api.model.OrganizationOnboardingRequestModel;
import com.myroom.onboardingservice.api.model.OrganizationOnboardingResponseModel;
import com.myroom.onboardingservice.data.dto.OrganizationAccountOnboardingRequestModelDto;
import com.myroom.onboardingservice.data.dto.OrganizationRequestDto;
import com.myroom.onboardingservice.data.dto.OrganizationResponseDto;
import org.springframework.stereotype.Component;

@Component
public class OrganizationMapper {
    public OrganizationOnboardingResponseModel toOrganizationOnboardingResponseModel(OrganizationResponseDto createdOrganizationData) {
        return  OrganizationOnboardingResponseModel.builder()
                .id(createdOrganizationData.getId())
                .name(createdOrganizationData.getName())
                .email(createdOrganizationData.getEmail())
                .description(createdOrganizationData.getDescription())
                .phone(createdOrganizationData.getPhone())
                .businessProfile(createdOrganizationData.getBusinessProfile())
                .superAdmin(createdOrganizationData.getSuperAdmin())
                .build();
    }

    public OrganizationRequestDto toOrganizationRequestDto(OrganizationOnboardingRequestModel onboardingRequestModel) {
        return OrganizationRequestDto.builder()
                .name(onboardingRequestModel.getName())
                .email(onboardingRequestModel.getEmail())
                .description(onboardingRequestModel.getDescription())
                .phone(onboardingRequestModel.getPhone())
                .businessProfile(onboardingRequestModel.getBusinessProfile())
                .uid(onboardingRequestModel.getUid())
                .build();
    }

    public OrganizationAccountOnboardingRequestModelDto toOrganizationAccountOnboardingRequestModelDto(OrganizationAccountOnboardingRequestModel organizationAccountOnboardingRequestModel) {
        return OrganizationAccountOnboardingRequestModelDto.builder()
                .uid(organizationAccountOnboardingRequestModel.getUid())
                .build();
    }
}