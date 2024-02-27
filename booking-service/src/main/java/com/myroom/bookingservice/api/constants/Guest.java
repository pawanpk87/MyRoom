package com.myroom.bookingservice.api.constants;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Guest {
    @Schema(description = "adults(13+)", example = "2", required = true)
    Integer adults;

    @Schema(description = "children(2 - 12)", example = "1", required = true)
    Integer children;
}
