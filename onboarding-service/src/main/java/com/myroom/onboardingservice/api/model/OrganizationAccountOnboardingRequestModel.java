package com.myroom.onboardingservice.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class OrganizationAccountOnboardingRequestModel {
    @Schema(description = "admin uid", example = "23fauidasdknf", required = true)
    private String uid;
}