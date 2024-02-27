package com.myroom.onboardingservice.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrganizationOnboardingResponseModel {
    @Schema(description = "Organization id", example = "658d6e2f9583cda663107426", required = true)
    private String id;

    @Schema(description = "Organization Name", example = "MyCompany", required = true)
    private String name;

    @Schema(description = "Organization email", example = "email@mycompany.com", required = true)
    private String email;

    @Schema(description = "Organization Description", example = "MyCompany Details", required = true)
    private String description;

    @Schema(description = "Organization Phone Number", example = "+91123456789", required = true)
    private String phone;

    @Schema(description = "Organization business profile", required = true)
    private BusinessProfile businessProfile;

    @Schema(description = "Organization superAdmin", required = true)
    private OrganizationAdmin superAdmin;
}