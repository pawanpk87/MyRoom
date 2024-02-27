package com.myroom.organizationpay.usecase.impl;

import com.myroom.organizationpay.api.constants.OrganizationAccountStatus;
import com.myroom.organizationpay.api.constants.stripe.StripeAccountStatus;
import com.myroom.organizationpay.data.entity.StripeAccount;
import com.myroom.organizationpay.usecase.AccountService;
import com.myroom.organizationpay.usecase.KafkaMessageService;
import com.myroom.organizationpay.usecase.OrganizationStripeAccountEventsService;
import com.myroom.organizationpay.usecase.StripeAccountService;
import com.stripe.model.StripeObject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class OrganizationStripeAccountEventsServiceImpl implements OrganizationStripeAccountEventsService {
    @Autowired
    AccountService accountService;

    @Autowired
    StripeAccountService stripeAccountService;

    @Autowired
    KafkaMessageService kafkaMessageService;


    @Override
    public void handleAccountUpdatedEvent(StripeObject stripeObject) {
        log.info("Received account.updated event with data: {}", stripeObject);

        JSONObject jsonObject = new JSONObject(stripeObject.getRawJsonObject());

        String accountId = jsonObject.getString("id");
        boolean isChargesEnabled = jsonObject.getBoolean("charges_enabled");
        boolean isPayoutsEnabled = jsonObject.getBoolean("payouts_enabled");

        JSONArray jsonArray = jsonObject.getJSONArray("requirements.errors");

        if(isChargesEnabled & isPayoutsEnabled){
            updateOrganizationAccountStatus(accountId, OrganizationAccountStatus.ACTIVE, StripeAccountStatus.ACTIVE, jsonObject);
        } else{
            updateOrganizationAccountStatus(accountId, OrganizationAccountStatus.INACTIVE, StripeAccountStatus.INACTIVE, jsonObject);
        }
    }

    @Transactional
    private void updateOrganizationAccountStatus(String accountId, OrganizationAccountStatus organizationAccountStatus, StripeAccountStatus stripeAccountStatus, JSONObject jsonObject) {
        log.info("Updating Organization account status to : {} and stripe account status to: {}, for stripe accountId: {}", organizationAccountStatus, stripeAccountStatus, accountId);

        StripeAccount stripeAccount = stripeAccountService.getAccountDetailsByAccountId(accountId);

        String organizationId = stripeAccount.getOrganizationRef();

        // Update Organization account status
        accountService.updateStatusByOrganizationId(organizationId, organizationAccountStatus);

        // Update Organization stripe account status
        stripeAccountService.updateStatusByOrganizationId(organizationId, stripeAccountStatus);

        // Send notification
        sendAccountStatusNotification(organizationId, organizationAccountStatus, jsonObject);
    }

    private void sendAccountStatusNotification(String organizationId, OrganizationAccountStatus organizationAccountStatus,  JSONObject stripeData) {
        JSONObject messagePayload = new JSONObject();
        messagePayload.put("type", "account.status");
        messagePayload.put("organizationId", organizationId);
        messagePayload.put("data", stripeData);

        kafkaMessageService.sendMessage("myRoom.notification", messagePayload.toString());
    }
}