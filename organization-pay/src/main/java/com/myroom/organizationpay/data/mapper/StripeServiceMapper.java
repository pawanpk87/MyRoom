package com.myroom.organizationpay.data.mapper;

import com.myroom.organizationpay.api.constants.Currency;
import com.myroom.organizationpay.data.dto.CreateStripeAccountLinkResponseDto;
import com.myroom.organizationpay.data.dto.StripeAccountDto;
import com.stripe.model.Account;
import com.stripe.model.AccountLink;
import org.springframework.stereotype.Component;

@Component
public class StripeServiceMapper {
    public StripeAccountDto toStripeAccount(Account account) {
        return StripeAccountDto.builder()
                .accountId(account.getId())
                .type(account.getType())
                .currency(Currency.valueOf(account.getDefaultCurrency()))
                .build();
    }

    public CreateStripeAccountLinkResponseDto toCreateStripeAccountLinkResponseDto(AccountLink accountLink) {
        return CreateStripeAccountLinkResponseDto.builder()
                .created(String.valueOf(accountLink.getCreated()))
                .expiresAt(String.valueOf(accountLink.getExpiresAt()))
                .url(accountLink.getUrl())
                .build();
    }
}