package com.myroom.paymentservice.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationAccountDetails {
    private String organizationId;

    private String status;

    private OrganizationStripeAccountDetails stripeAccountDetails;
}
