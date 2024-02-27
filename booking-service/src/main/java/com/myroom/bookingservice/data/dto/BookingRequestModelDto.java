package com.myroom.bookingservice.data.dto;

import com.myroom.bookingservice.api.constants.ContactDetails;
import com.myroom.bookingservice.api.constants.Guest;
import com.myroom.bookingservice.api.constants.PaymentMethodType;
import com.myroom.bookingservice.api.constants.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequestModelDto {
    String checkIn;

    String checkOut;

    Guest guests;

    String roomId;

    String uid;

    ContactDetails contactDetails;

    String organizationId;

    PaymentType paymentType;

    PaymentMethodType paymentMethodType;
}