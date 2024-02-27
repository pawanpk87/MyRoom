package com.myroom.bookingservice.data.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomMetaDataModel{
    String id;

    String title;

    String organizationId;

    RoomPriceMetaDataModel prices;
}
