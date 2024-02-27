package com.myroom.onboardingservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myroom.onboardingservice.event.OrganizationOnboardingEvent;
import com.myroom.onboardingservice.usecase.KafkaMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrganizationOnboardingEventListener {
    @Autowired
    KafkaMessageService kafkaMessageService;

    @Autowired
    ObjectMapper objectMapper;


    @EventListener
    public void handleBookingEvent(OrganizationOnboardingEvent organizationOnboardingEvent){
        switch (organizationOnboardingEvent.getEventType()) {
            case ACCOUNT_CREATED:
                log.info("Sending message to kafka");
                // Handle account created event
                // TODO: Send message to Kafka for account creation
                // kafkaMessageService.sendMessage("myRoom.joined", objectMapper.writeValueAsString(organizationOnboardingEvent.getOrganizationDetails()));
                break;

            case BANK_ACCOUNT_CREATED:
                // Handle bank account created event
                // TODO: Send message to Kafka for bank account creation
                break;

            default:
                // Handle other event types if needed
                break;
        }
    }
}