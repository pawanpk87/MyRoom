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
public class Addresses {
    @Schema(description = "street1", example = "Whitefield", required = true)
    private String street1;

    @Schema(description = "street2", example = "Whitefield")
    private String street2;

    @Schema(description = "city", example = "Bengaluru", required = true)
    private String city;

    @Schema(description = "state", example = "KARNATAKA", required = true)
    private String state;

    @Schema(description = "postalCode", example = "560034", required = true)
    private String postalCode;

    @Schema(description = "country", example = "Whitefield", defaultValue = "IN")
    private String country;
}
