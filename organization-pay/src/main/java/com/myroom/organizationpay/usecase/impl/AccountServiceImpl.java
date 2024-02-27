package com.myroom.organizationpay.usecase.impl;

import com.myroom.organizationpay.api.constants.ApiConstants;
import com.myroom.organizationpay.api.constants.OrganizationAccountStatus;
import com.myroom.organizationpay.data.dto.StripeAccountDto;
import com.myroom.organizationpay.data.entity.OrganizationAccount;
import com.myroom.organizationpay.data.entity.StripeAccount;
import com.myroom.organizationpay.exception.AccountCreationFailed;
import com.myroom.organizationpay.exception.OrganizationPayServiceRuntimeException;
import com.myroom.organizationpay.repository.OrganizationAccountRepository;
import com.myroom.organizationpay.usecase.AccountService;
import com.myroom.organizationpay.usecase.StripeService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {
    @Autowired
    StripeService stripeService;

    @Autowired
    OrganizationAccountRepository organizationAccountRepository;


    @Override
    public Boolean isAccountExists(String organizationId) {
        log.info("Checking organization account existence for organizationId: {}", organizationId);
        try{
            Boolean isExist = organizationAccountRepository.existsByOrganizationRef(organizationId);
            log.info("Account exists for organization ID {}: {}", organizationId, isExist);
            return isExist;
        }catch (Exception ex){
            log.error("Failed to check account existence for organizationId: {}, error: {}", organizationId, ex.getMessage());
            throw new OrganizationPayServiceRuntimeException("Failed to create organization account");
        }
    }

    @Override
    public OrganizationAccount createAccount(String organizationId) {
        // Create Organization's Stripe Account
        StripeAccountDto stripeAccountDto = stripeService.createStripeAccount(organizationId);
        // Create Organization Account
        OrganizationAccount organizationAccount = createAccount(organizationId, stripeAccountDto);
        return organizationAccount;
    }

    @Override
    public void updateStatusByOrganizationId(String organizationId, OrganizationAccountStatus organizationAccountStatus) {
        log.info("Updating organization account status to: {}", organizationAccountStatus);
        try{
            organizationAccountRepository.updateStatusByOrganizationId(organizationId, organizationAccountStatus);
            log.info("Updated organization status successfully");
        }catch (Exception ex){
            log.error("Failed to update the account status, error: {}", ex.getMessage());
            throw new OrganizationPayServiceRuntimeException(ApiConstants.MESSAGE_INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    private OrganizationAccount createAccount(String organizationId, StripeAccountDto stripeAccountDto) {
        log.info("Creating organization account for organizationId: {} with stripeAccount Details: {}", organizationId, stripeAccountDto);
        try {
            StripeAccount stripeAccount = StripeAccount.builder()
                    .accountId(stripeAccountDto.getAccountId())
                    .type(stripeAccountDto.getType())
                    .organizationRef(stripeAccountDto.getOrganizationId())
                    .currency(stripeAccountDto.getCurrency())
                    .status(stripeAccountDto.getStatus())
                    .build();

            OrganizationAccount organizationAccount = OrganizationAccount.builder()
                    .organizationRef(organizationId)
                    .stripeAccountRef(stripeAccount)
                    .status(OrganizationAccountStatus.INACTIVE)
                    .build();

            OrganizationAccount savedAccount = organizationAccountRepository.save(organizationAccount);

            log.info("Successfully created organization account: {}", savedAccount);
            return savedAccount;
        }catch (Exception ex){
            log.error("Failed to create organization account: {}", ex.getMessage());
            throw new AccountCreationFailed(ApiConstants.ACCOUNT_CREATION_FAILED, "Failed to create organization account", ex.getMessage());
        }
    }
}