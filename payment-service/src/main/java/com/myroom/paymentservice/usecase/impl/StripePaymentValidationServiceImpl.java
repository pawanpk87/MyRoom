package com.myroom.paymentservice.usecase.impl;

import com.myroom.paymentservice.api.constants.ApiConstants;
import com.myroom.paymentservice.exception.OrganizationAccountNotActiveException;
import com.myroom.paymentservice.exception.OrganizationPayServiceException;
import com.myroom.paymentservice.usecase.OrganizationPayService;
import com.myroom.paymentservice.usecase.StripePaymentValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StripePaymentValidationServiceImpl implements StripePaymentValidationService {
    @Autowired
    OrganizationPayService organizationPayService;


    @Override
    public Boolean validateOrganizationAccountActive(String organizationId) {
        log.info("Validating Organization Account");
        Boolean isAccountActive = organizationPayService.isOrganizationAccountActive(organizationId);
        if(isAccountActive){
            log.info("Validation completed Organization Account is active");
            return true;
        }else{
            log.error("Organization account is not active");
            throw new OrganizationPayServiceException(
                    ApiConstants.ORGANIZATION_ACCOUNT_NOT_ACTIVE,
                    "Payment processing is currently unavailable as the organization's account is inactive.",
                    ""
            );
        }
    }
}
