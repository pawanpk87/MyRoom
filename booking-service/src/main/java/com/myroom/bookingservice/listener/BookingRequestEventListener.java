package com.myroom.bookingservice.listener;

import com.myroom.bookingservice.event.BookingRequestEvent;
import com.myroom.bookingservice.usecase.KafkaMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BookingRequestEventListener {
    @Autowired
    KafkaMessageService kafkaMessageService;


    @EventListener
    public void handleBookingRequestEvent(BookingRequestEvent bookingRequestEvent){
        switch (bookingRequestEvent.getEventType()) {
            case CREATED:
                // Handle created event
                // TODO: Send message to Kafka for created event
                break;

            case UPDATED:
                // Handle updated event
                // TODO: Send message to Kafka for updated event
                break;

            case CANCELLED:
                // Handle deleted event
                // TODO: Send message to Kafka for canceled event
                break;

            default:
                // Handle other event types if needed
                break;
        }
    }
}
