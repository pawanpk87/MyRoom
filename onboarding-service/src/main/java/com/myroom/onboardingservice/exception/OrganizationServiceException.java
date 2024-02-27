package com.myroom.onboardingservice.exception;

import lombok.Data;

@Data
public class OrganizationServiceException extends RuntimeException{
    private final String errorCode;
    private final String message;
    private final String details;

    public OrganizationServiceException(String errorCode, String message, String details){
        super(message);
        this.errorCode = errorCode;
        this.message = message;
        this.details = details;
    }
}