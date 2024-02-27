package com.myroom.organizationpay.exception;

import com.myroom.organizationpay.api.constants.ApiConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
@Slf4j
public class OrganizationPayExceptionHandler {
    @ExceptionHandler({ OrganizationAccountNotFoundException.class })
    public ResponseEntity<OrganizationPayWebException> handleOrganizationServiceException(OrganizationAccountNotFoundException exception, WebRequest request){
        log.error("Error: {}", exception);
        return new ResponseEntity<>(new OrganizationPayWebException(exception.getErrorCode(), exception.getMessage(), exception.getDetails()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ OrganizationOnboardingCompletedException.class })
    public ResponseEntity<OrganizationPayWebException> handleOrganizationServiceException(OrganizationOnboardingCompletedException exception, WebRequest request){
        log.error("Error: {}", exception);
        return new ResponseEntity<>(new OrganizationPayWebException(exception.getErrorCode(), exception.getMessage(), exception.getDetails()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ StripeServiceException.class })
    public ResponseEntity<OrganizationPayWebException> handleOrganizationServiceException(StripeServiceException exception, WebRequest request){
        log.error("Error: {}", exception);
        return new ResponseEntity<>(new OrganizationPayWebException(exception.getErrorCode(), exception.getMessage(), exception.getDetails()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ OrganizationServiceException.class })
    public ResponseEntity<OrganizationPayWebException> handleOrganizationServiceException(OrganizationServiceException exception, WebRequest request){
        log.error("Error: {}", exception);
        return new ResponseEntity<>(new OrganizationPayWebException(exception.getErrorCode(), exception.getMessage(), exception.getDetails()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<OrganizationPayWebException> handleException(Exception exception, WebRequest request){
        log.error("Error: {}", exception.getMessage());
        return new ResponseEntity<>(new OrganizationPayWebException(ApiConstants.INTERNAL_SERVER_ERROR, exception.getMessage(), ApiConstants.MESSAGE_INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}