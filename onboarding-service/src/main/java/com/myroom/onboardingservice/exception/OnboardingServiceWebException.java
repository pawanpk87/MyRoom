package com.myroom.onboardingservice.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonIgnoreProperties({"stackTrace", "cause", "suppressed", "localizedMessage"})
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class OnboardingServiceWebException {
    private final String errorCode;
    private final String message;
    private final String details;

    public OnboardingServiceWebException(String errorCode, String message, String details){
        this.errorCode = errorCode;
        this.message = message;
        this.details = details;
    }
}