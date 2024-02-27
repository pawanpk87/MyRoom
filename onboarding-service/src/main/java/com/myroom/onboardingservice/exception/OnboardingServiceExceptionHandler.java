package com.myroom.onboardingservice.exception;

import com.myroom.onboardingservice.api.constants.ApiConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
@Slf4j
public class OnboardingServiceExceptionHandler {
    @ExceptionHandler({ OrganizationServiceException.class })
    public ResponseEntity<OnboardingServiceWebException> handleOrganizationServiceException(OrganizationServiceException exception, WebRequest request){
        log.error("Error: {}", exception);
        return new ResponseEntity<>(new OnboardingServiceWebException(exception.getErrorCode(), exception.getMessage(), exception.getDetails()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ OnboardingServiceRuntimeException.class })
    public ResponseEntity<OnboardingServiceWebException> handleException(Exception exception, WebRequest request){
        log.error("Error: {}", exception.getMessage());
        return new ResponseEntity<>(new OnboardingServiceWebException(ApiConstants.INTERNAL_SERVER_ERROR, exception.getMessage(), ApiConstants.MESSAGE_INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}