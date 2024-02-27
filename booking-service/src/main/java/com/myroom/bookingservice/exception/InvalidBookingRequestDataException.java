package com.myroom.bookingservice.exception;

import lombok.Data;

@Data
public class InvalidBookingRequestDataException extends RuntimeException{
    private final String errorCode;
    private final String message;
    private final String details;

    public InvalidBookingRequestDataException(String errorCode, String message, String details){
        super(message);
        this.errorCode = errorCode;
        this.message = message;
        this.details = details;
    }
}
