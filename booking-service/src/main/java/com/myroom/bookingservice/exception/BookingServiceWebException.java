package com.myroom.bookingservice.exception;

import lombok.Data;

@Data
public class BookingServiceWebException {
    private final String errorCode;
    private final String message;
    private final String details;

    public BookingServiceWebException(String errorCode, String message, String details){
        this.errorCode = errorCode;
        this.message = message;
        this.details = details;
    }
}
