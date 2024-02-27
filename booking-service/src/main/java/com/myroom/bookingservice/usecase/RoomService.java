package com.myroom.bookingservice.usecase;

import com.myroom.bookingservice.data.dto.RoomAvailabilityRequest;
import com.myroom.bookingservice.data.dto.RoomDetailsDto;

public interface RoomService {
    boolean checkAvailability(RoomAvailabilityRequest roomAvailabilityRequest);

    RoomDetailsDto getRoom(String roomId);
}