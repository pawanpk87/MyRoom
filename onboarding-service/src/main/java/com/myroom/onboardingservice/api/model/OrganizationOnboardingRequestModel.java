package com.myroom.onboardingservice.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class OrganizationOnboardingRequestModel {
    @Schema(description = "Organization Name", example = "MyCompany", required = true)
    private String name;

    @Schema(description = "Organization email", example = "email@mycompany.com", required = true)
    private String email;

    @Schema(description = "Organization Phone Number", example = "+91123456789", required = true)
    private String phone;

    @Schema(description = "Organization Description", example = "MyCompany Details", required = true)
    private String description;

    @Schema(description = "Organization business profile", required = true)
    private BusinessProfile businessProfile;

    @Schema(description = "User id", example = "", required = true)
    private String uid;
}