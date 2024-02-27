package com.myroom.paymentservice.usecase;

public interface KafkaMessageService {
    void sendMessage(String topic, String message);
}
