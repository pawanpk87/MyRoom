package com.myroom.organizationpay.exception;

import lombok.Data;

@Data
public class AccountCreationFailed extends RuntimeException{
    private final String errorCode;
    private final String message;
    private final String details;

    public AccountCreationFailed(String errorCode, String message, String details){
        super(message);
        this.errorCode = errorCode;
        this.message = message;
        this.details = details;
    }
}
