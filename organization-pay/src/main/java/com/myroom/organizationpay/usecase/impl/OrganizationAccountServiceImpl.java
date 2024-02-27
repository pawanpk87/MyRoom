package com.myroom.organizationpay.usecase.impl;

import com.myroom.organizationpay.api.constants.ApiConstants;
import com.myroom.organizationpay.api.model.AccountDetailsResponseModel;
import com.myroom.organizationpay.data.entity.OrganizationAccount;
import com.myroom.organizationpay.data.mapper.OrganizationAccountMapper;
import com.myroom.organizationpay.exception.OrganizationAccountNotFoundException;
import com.myroom.organizationpay.repository.OrganizationAccountRepository;
import com.myroom.organizationpay.usecase.OrganizationAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class OrganizationAccountServiceImpl implements OrganizationAccountService {
    @Autowired
    OrganizationAccountRepository organizationAccountRepository;

    @Autowired
    OrganizationAccountMapper organizationAccountMapper;


    @Override
    public AccountDetailsResponseModel getAccountDetails(String organizationId) {
        log.info("Fetching OrganizationAccount for: {}",organizationId);

        Optional<OrganizationAccount> organizationAccount = organizationAccountRepository.findByOrganizationId(organizationId);
        if (organizationAccount.isEmpty()){
            log.error("No account was found for the organization with the ID: {}",organizationId);
            throw new OrganizationAccountNotFoundException(ApiConstants.ORGANIZATION_ACCOUNT_NOT_FOUND, "No account was found for the organization with the ID: "+organizationId, "");
        }

        log.info("Fetched OrganizationAccount:{}", organizationAccount);

        AccountDetailsResponseModel accountDetailsResponseModel = organizationAccountMapper.toAccountDetailsResponseModel(organizationAccount.get());

        return accountDetailsResponseModel;
    }

    @Override
    public OrganizationAccount getOrganizationAccountDetails(String organizationId) {
        log.info("Fetching OrganizationAccount for organizationId: {}",organizationId);

        Optional<OrganizationAccount> organizationAccount = organizationAccountRepository.findByOrganizationId(organizationId);
        if (organizationAccount.isEmpty()){
            log.error("No account was found for the organization with the ID: {}",organizationId);
            throw new OrganizationAccountNotFoundException(ApiConstants.ORGANIZATION_ACCOUNT_NOT_FOUND, "No account was found for the organization with the ID: "+organizationId, null);
        }

        log.info("Fetched OrganizationAccount:{}", organizationAccount);

        return organizationAccount.get();
    }
}
