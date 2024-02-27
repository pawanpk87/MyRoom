package com.myroom.bookingservice.exception;

import lombok.Data;

@Data
public class BookingServiceRuntimeException extends RuntimeException{
    public BookingServiceRuntimeException(String message){
        super(message);
    }
}
