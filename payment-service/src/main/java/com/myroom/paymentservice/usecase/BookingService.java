package com.myroom.paymentservice.usecase;

import com.myroom.paymentservice.data.dto.BookingDataResponseDto;

public interface BookingService {
    BookingDataResponseDto getBookingDetails(String bookingId);
}
