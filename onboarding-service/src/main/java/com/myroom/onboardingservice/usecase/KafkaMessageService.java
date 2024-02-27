package com.myroom.onboardingservice.usecase;

public interface KafkaMessageService {
    void sendMessage(String topic, String message);
}
