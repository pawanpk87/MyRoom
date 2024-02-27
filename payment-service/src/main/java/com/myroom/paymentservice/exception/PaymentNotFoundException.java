package com.myroom.paymentservice.exception;

import lombok.Data;

@Data
public class PaymentNotFoundException extends  RuntimeException{
    private final String errorCode;
    private final String message;
    private final String details;

    public PaymentNotFoundException(String errorCode, String message, String details){
        super(message);
        this.errorCode = errorCode;
        this.message = message;
        this.details = details;
    }
}
