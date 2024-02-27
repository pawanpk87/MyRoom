package com.myroom.paymentservice.exception;

public class PaymentServiceRuntimeException extends RuntimeException{
    public PaymentServiceRuntimeException(String message){
        super(message);
    }
}
