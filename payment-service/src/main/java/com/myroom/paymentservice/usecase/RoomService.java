package com.myroom.paymentservice.usecase;

import com.myroom.paymentservice.data.dto.UpdateRoomStatusPayload;

public interface RoomService {
    void updateRoomStatus(UpdateRoomStatusPayload updateRoomStatusPayload);
}