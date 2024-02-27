package com.myroom.bookingservice.usecase.impl;

import com.myroom.bookingservice.usecase.KafkaMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaMessageServiceImpl implements KafkaMessageService {
    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void sendMessage(String topic, String message) {
        try {
            log.info("Sending a message to the Kafka topic {} with the content: {}", topic, message);
            kafkaTemplate.send(topic, message);
            log.info("Sent the message!");
        }catch (Exception exception){
            log.error("When pushing the message to the Kafka topic, an exception happened. {}", exception.getMessage());
        }
    }
}
