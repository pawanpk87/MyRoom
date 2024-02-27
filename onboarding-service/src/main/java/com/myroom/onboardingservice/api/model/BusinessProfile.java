package com.myroom.onboardingservice.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BusinessProfile {
    @Schema(description = "organization business type", example = "individual", required = true, defaultValue = "individual")
    private String businessType;

    @Schema(description = "organization category", example = "housing", required = true, defaultValue = "housing")
    private String category;

    @Schema(description = "organization subcategory", example = "space_rental", required = true, defaultValue = "space_rental")
    private String subcategory;

    @Schema(description = "addresses", required = true)
    private Addresses addresses;
}
