package com.myroom.organizationpay.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.myroom.organizationpay.api.constants.Currency;
import com.myroom.organizationpay.api.constants.stripe.StripeAccountStatus;
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
public class StripeAccountDto {
    private String accountId;

    private String organizationId;

    private String type;

    private Currency currency;

    private StripeAccountStatus status;
}
