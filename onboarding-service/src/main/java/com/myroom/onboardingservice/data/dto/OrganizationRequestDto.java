package com.myroom.onboardingservice.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.myroom.onboardingservice.api.model.BusinessProfile;
import com.myroom.onboardingservice.api.model.OrganizationAdmin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrganizationRequestDto {
    private String name;

    private String email;

    private String description;

    private String phone;

    private BusinessProfile businessProfile;

    private String uid;
}
