package com.myroom.bookingservice.usecase;

public interface KafkaMessageService {
    void sendMessage(String topic, String message);
}