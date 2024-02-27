package com.myroom.organizationpay.usecase.impl;

import com.myroom.organizationpay.api.constants.ApiConstants;
import com.myroom.organizationpay.api.constants.stripe.StripeAccountStatus;
import com.myroom.organizationpay.data.dto.CreateStripeAccountLinkResponseDto;
import com.myroom.organizationpay.data.dto.OrganizationResponseDto;
import com.myroom.organizationpay.data.dto.StripeAccountDto;
import com.myroom.organizationpay.data.entity.OrganizationAccount;
import com.myroom.organizationpay.data.mapper.StripeServiceMapper;
import com.myroom.organizationpay.exception.OrganizationPayServiceRuntimeException;
import com.myroom.organizationpay.exception.StripeServiceException;
import com.myroom.organizationpay.usecase.OrganizationAccountService;
import com.myroom.organizationpay.usecase.OrganizationService;
import com.myroom.organizationpay.usecase.OrganizationStripeAccountEventsService;
import com.myroom.organizationpay.usecase.StripeService;
import com.myroom.organizationpay.util.EurekaClientUtil;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.net.Webhook;
import com.stripe.param.AccountCreateParams;
import com.stripe.param.AccountLinkCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.google.gson.JsonSyntaxException;

import java.util.Map;

@Service
@Slf4j
public class StripeServiceImpl implements StripeService {
    @Autowired
    OrganizationService organizationService;

    @Autowired
    OrganizationAccountService organizationAccountService;

    @Autowired
    OrganizationStripeAccountEventsService organizationStripeAccountEventsService;

    @Autowired
    StripeServiceMapper stripeServiceMapper;

    @Autowired
    EurekaClientUtil eurekaClientUtil;

    @Value("${stripe.api.secret-key}")
    String SecretKey;

    @Value("${stripe.endpointSecret}")
    String endpointSecret;

    @Value("${services.organization-pay.id}")
    String organizationPayServiceID;

    @Value("${services.organization-pay.v1.api}")
    String organizationPayServiceV1;


    @PostConstruct
    private void initialize(){
        // set the stripe secret api key
        Stripe.apiKey = SecretKey;
    }

    @Override
    public StripeAccountDto createStripeAccount(String organizationId) {
        log.info("Creating stripe account for organizationId: {}", organizationId);
        OrganizationResponseDto organizationData = organizationService.getOrganization(organizationId);
        try {
            AccountCreateParams params =
                    AccountCreateParams.builder()
                            .setType(AccountCreateParams.Type.STANDARD)
                            .setCompany(AccountCreateParams.Company.builder()
                                    .setName(organizationData.getName())
                                    .setPhone(organizationData.getPhone())
                                    .build())
                            .setCountry(organizationData.getBusinessProfile().getAddresses().getCountry())
                            .setEmail(organizationData.getEmail())
                            .setBusinessType(AccountCreateParams.BusinessType.valueOf(organizationData.getBusinessProfile().getBusinessType().toUpperCase()))
                            .setDefaultCurrency("INR")
                            .build();
            Account account = Account.create(params);
            log.info("Stripe account created successfully: {}", account);
            StripeAccountDto stripeAccountDto = stripeServiceMapper.toStripeAccount(account);
            stripeAccountDto.setOrganizationId(organizationId);
            stripeAccountDto.setStatus(StripeAccountStatus.ACCOUNT_ONBOARDING_PENDING);
            return stripeAccountDto;
        } catch (StripeException stripeException){
            log.error("Failed to create stripe account: {}", stripeException);
            throw new StripeServiceException(stripeException.getCode(), stripeException.getUserMessage(),  stripeException.getMessage());
        } catch (Exception ex){
            log.error("Some error curred: {}", ex.getMessage());
            throw new OrganizationPayServiceRuntimeException(ex.getMessage());
        }
    }

    @Override
    public StripeAccountDto getAccount(String accountId) {
        log.info("Fetching stripe account data for accountId: {}", accountId);
        try {
            Account account = Account.retrieve(accountId);
            log.info("Fetched Stripe account successfully: {}", account);
            return stripeServiceMapper.toStripeAccount(account);
        } catch (StripeException stripeException){
            log.error("Failed to retrieve stripe account: {}", stripeException);
            throw new StripeServiceException(stripeException.getCode(), stripeException.getUserMessage(),  stripeException.getMessage());
        } catch (Exception ex){
            log.error("Some error curred: {}", ex.getMessage());
            throw new OrganizationPayServiceRuntimeException(ApiConstants.MESSAGE_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CreateStripeAccountLinkResponseDto createStripeAccountLinkByStripeAccountId(String accountId, AccountLinkCreateParams.Type type) {
        log.info("Creating stripe account link for accountId: {} with type: {}", accountId, type);
        String refreshUrl = getRefreshUrl(accountId);
        String returnUrl = getReturnUrl(accountId);
        try {
            AccountLinkCreateParams params =
                    AccountLinkCreateParams.builder()
                            .setAccount(accountId)
                            .setRefreshUrl(refreshUrl)
                            .setReturnUrl(returnUrl)
                            .setType(type)
                            .build();
            AccountLink accountLink = AccountLink.create(params);
            log.info("Created Stripe account link successfully: {}", accountLink);
            return stripeServiceMapper.toCreateStripeAccountLinkResponseDto(accountLink);
        } catch (StripeException stripeException){
            log.error("Failed to create stripe account link: {}", stripeException);
            throw new StripeServiceException(stripeException.getCode(), stripeException.getUserMessage(),  stripeException.getMessage());
        } catch (Exception ex){
            log.error("Some error curred: {}", ex.getMessage());
            throw new OrganizationPayServiceRuntimeException(ApiConstants.MESSAGE_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CreateStripeAccountLinkResponseDto createStripeAccountLinkByStripeOrganizationId(String organizationId, AccountLinkCreateParams.Type type) {
        log.info("Creating stripe account link for organizationId: {} with type: {}", organizationId, type);
        OrganizationAccount organizationAccountDetails = organizationAccountService.getOrganizationAccountDetails(organizationId);
        return createStripeAccountLinkByStripeAccountId(organizationAccountDetails.getStripeAccountRef().getAccountId(), type);
    }

    @Override
    public void handleEvent(String payload, Map<String, String> headers) {
        log.info("Received Stripe Webhook event with payload: {} and headers: {}", payload, headers);
        Event event = getEventObject(payload, headers);
        StripeObject stripeObject = getStripeObject(event);
        log.info("Handling event");
        switch (event.getType()){
            case "account.updated":{
                organizationStripeAccountEventsService.handleAccountUpdatedEvent(stripeObject);
                break;
            }
            default:

        }
    }

    private StripeObject getStripeObject(Event event) {
        log.info("Fetching StripeObject from event");
        log.info("Deserializing the nested object inside event");
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        if(dataObjectDeserializer.getObject().isPresent()){
            StripeObject stripeObject = dataObjectDeserializer.getObject().get();
            log.info("Fetched StripeObject: {}", stripeObject);
            return stripeObject;
        }else{
            log.error("Deserialization failed");
            throw new OrganizationPayServiceRuntimeException(ApiConstants.MESSAGE_INTERNAL_SERVER_ERROR);
        }
    }

    private Event getEventObject(String payload, Map<String, String> headers) {
        log.info("Fetching event object from stipe webhook event");
        String sigHeader = headers.get("stripe-signature");
        try {
            Event event = Webhook.constructEvent(
                    payload, sigHeader, endpointSecret
            );
            log.info("Fetched event object: {}", event);
            return event;
        } catch (JsonSyntaxException ex){
            log.error("Invalid payload");
            throw new OrganizationPayServiceRuntimeException(ApiConstants.MESSAGE_INTERNAL_SERVER_ERROR);
        } catch (SignatureVerificationException ex){
            log.error("Invalid signature");
            throw new OrganizationPayServiceRuntimeException(ApiConstants.MESSAGE_INTERNAL_SERVER_ERROR);
        }
    }

    private String getRefreshUrl(String accountId){
        String organizationServiceURI = eurekaClientUtil.getServiceUri(organizationPayServiceID);
        String url = organizationServiceURI + organizationPayServiceV1 + "/onboarding?accountId=" + accountId;
        return url;
    }

    private String getReturnUrl(String accountId){
        String organizationServiceURI = eurekaClientUtil.getServiceUri(organizationPayServiceID);
        String url = organizationServiceURI + organizationPayServiceV1 + "/onboarding/verify?accountId=" + accountId;
        return url;
    }
}
