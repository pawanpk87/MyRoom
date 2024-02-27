package com.myroom.paymentservice.data.dto;

import com.myroom.paymentservice.api.constants.BookingStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateRoomStatusPayload {
    private String id;

    private String uid;

    BookingStatus bookingStatus;
}
