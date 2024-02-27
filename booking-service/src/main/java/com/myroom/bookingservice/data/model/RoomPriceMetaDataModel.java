package com.myroom.bookingservice.data.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomPriceMetaDataModel {
    private String price;

    private String cleaningFee;

    private String roomService;

    private String currency;
}
