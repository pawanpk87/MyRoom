package com.myroom.paymentservice.exception;

import lombok.Data;

@Data
public class InvalidPriceException extends RuntimeException{
    private final String errorCode;
    private final String message;
    private final String details;

    public InvalidPriceException(String errorCode, String message, String details){
        super(message);
        this.errorCode = errorCode;
        this.message = message;
        this.details = details;
    }
}
