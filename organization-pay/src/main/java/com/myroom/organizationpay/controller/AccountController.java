package com.myroom.organizationpay.controller;

import com.myroom.organizationpay.api.model.AccountDetailsResponseModel;
import com.myroom.organizationpay.api.resource.AccountResource;
import com.myroom.organizationpay.usecase.OrganizationAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class AccountController implements AccountResource {
    @Autowired
    OrganizationAccountService organizationAccountService;


    @Override
    public ResponseEntity<AccountDetailsResponseModel> getAccountDetailsByOrganizationId(String organizationId) {
        log.info("Received get organization account details request");
        log.info("Getting Organization Account Details for organizationId: {}", organizationId);
        AccountDetailsResponseModel accountDetailsResponseModel = organizationAccountService.getAccountDetails(organizationId);
        log.info("Fetched Organization Account Details: {}", accountDetailsResponseModel);
        return new ResponseEntity<>(accountDetailsResponseModel, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Boolean> checkAccountActive(String organizationId) {
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}
