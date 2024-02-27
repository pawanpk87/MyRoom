package com.myroom.bookingservice.event;

import com.myroom.bookingservice.data.entity.BookingDetails;
import org.springframework.context.ApplicationEvent;

public class BookingEvent extends ApplicationEvent {

    public enum EventType{
        CREATED, UPDATED, CANCELLED, CHECKOUT, CHECKIN
    }

    private final BookingDetails bookingDetails;
    private final BookingEvent.EventType eventType;

    public BookingEvent(Object source, BookingEvent.EventType eventType, BookingDetails bookingDetails) {
        super(source);
        this.eventType = eventType;
        this.bookingDetails = bookingDetails;
    }

    public BookingDetails getBookingDetails() {
        return bookingDetails;
    }

    public BookingEvent.EventType getEventType() {
        return eventType;
    }
}