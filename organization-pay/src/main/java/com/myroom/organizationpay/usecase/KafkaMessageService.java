package com.myroom.organizationpay.usecase;

public interface KafkaMessageService {
    void sendMessage(String topic, String message);
}
