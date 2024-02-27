package com.myroom.onboardingservice.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationAdmin {
    @Schema(description = "admin uid", example = "zwtWhso1vTeEhPC9iBLfM5KMfdF3", required = true)
    private String uid;

    @Schema(description = "admin type", example = "SUPER_ADMIN/ADMIN", required = true)
    private String adminType;
}

