package com.myroom.bookingservice.event;

import com.myroom.bookingservice.data.entity.BookingRequestDetails;
import org.springframework.context.ApplicationEvent;

public class BookingRequestEvent extends ApplicationEvent {
    public enum EventType{
        CREATED, UPDATED, CANCELLED
    }

    private final BookingRequestDetails bookingRequestDetails;
    private final EventType eventType;

    public BookingRequestEvent(Object source, EventType eventType, BookingRequestDetails bookingRequestDetails) {
        super(source);
        this.bookingRequestDetails = bookingRequestDetails;
        this.eventType = eventType;
    }

    public BookingRequestDetails getBookingRequestDetails() {
        return bookingRequestDetails;
    }

    public EventType getEventType(){
        return eventType;
    }
}