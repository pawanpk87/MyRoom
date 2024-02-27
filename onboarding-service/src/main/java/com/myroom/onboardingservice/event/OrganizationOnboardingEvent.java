package com.myroom.onboardingservice.event;

import com.myroom.onboardingservice.data.dto.OrganizationResponseDto;
import org.springframework.context.ApplicationEvent;

public class OrganizationOnboardingEvent extends ApplicationEvent {
    public enum EventType{
        ACCOUNT_CREATED, BANK_ACCOUNT_CREATED
    }

    private final OrganizationResponseDto organizationDetails;
    private final EventType eventType;

    public OrganizationOnboardingEvent(Object source, EventType eventType, OrganizationResponseDto organizationDetails) {
        super(source);
        this.eventType = eventType;
        this.organizationDetails = organizationDetails;
    }

    public OrganizationResponseDto getOrganizationDetails() {
        return organizationDetails;
    }

    public EventType getEventType() {
        return eventType;
    }
}