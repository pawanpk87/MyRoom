package com.myroom.paymentservice.exception;

import com.myroom.paymentservice.api.constants.ApiConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
@Slf4j
public class PaymentServiceExceptionHandler {
    @ExceptionHandler({ OrganizationAccountNotFoundException.class})
    public ResponseEntity<PaymentServiceWebException> handleOrganizationPayServiceException(OrganizationAccountNotFoundException exception, WebRequest request){
        log.error("Error: {}", exception.getMessage());
        return new ResponseEntity<>(new PaymentServiceWebException(exception.getErrorCode(), exception.getMessage(), exception.getDetails()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ OrganizationPayServiceException.class})
    public ResponseEntity<PaymentServiceWebException> handleOrganizationPayServiceException(OrganizationPayServiceException exception, WebRequest request){
        log.error("Error: {}", exception.getMessage());
        return new ResponseEntity<>(new PaymentServiceWebException(exception.getErrorCode(), exception.getMessage(), exception.getDetails()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ OrganizationAccountNotActiveException.class})
    public ResponseEntity<PaymentServiceWebException> handleOrganizationAccountNotActiveException(OrganizationAccountNotActiveException exception, WebRequest request){
        log.error("Error: {}", exception.getMessage());
        return new ResponseEntity<>(new PaymentServiceWebException(exception.getErrorCode(), exception.getMessage(), exception.getDetails()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ InvalidPriceException.class})
    public ResponseEntity<PaymentServiceWebException> handleInvalidPriceExceptionException(InvalidPriceException exception, WebRequest request){
        log.error("Error: {}", exception.getMessage());
        return new ResponseEntity<>(new PaymentServiceWebException(exception.getErrorCode(), exception.getMessage(), exception.getDetails()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ Exception.class, PaymentServiceRuntimeException.class})
    public ResponseEntity<PaymentServiceWebException> handleException(Exception exception, WebRequest request){
        log.error("Error: {}", exception.getMessage());
        return new ResponseEntity<>(new PaymentServiceWebException(ApiConstants.INTERNAL_SERVER_ERROR, ApiConstants.MESSAGE_INTERNAL_SERVER_ERROR, exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}