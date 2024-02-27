package com.myroom.paymentservice.api.constants;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PaymentNote {
    @Schema(description = "note key", example = "uid", required = true)
    private String key;

    @Schema(description = "note value", example = "8723tr875baw673", required = true)
    private String value;
}
