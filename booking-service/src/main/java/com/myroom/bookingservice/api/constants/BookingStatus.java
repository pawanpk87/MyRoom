package com.myroom.bookingservice.api.constants;

import jdk.jfr.Description;

public enum BookingStatus {
    @Description("Booking is created but payment is pending.")
    PENDING_PAYMENT,

    @Description("To be paid at the hotel upon arrival.")
    PAY_AT_HOTEL,

    @Description("Booking is confirmed after successful payment.")
    CONFIRMED,

    @Description("Guest has checked in to the property or started using service.")
    CHECKED_IN,

    @Description("Guest has checked out.")
    CHECKED_OUT,

    @Description("Booking is marked as no-show.")
    NO_SHOW,

    @Description("Booking is cancelled by the user before payment.")
    CANCELLED,

    @Description("Expired due to payment not being completed within a specified timeframe.")
    EXPIRED
}