package com.myroom.bookingservice.data.mapper;

import com.myroom.bookingservice.api.model.BookingDataResponseModel;
import com.myroom.bookingservice.api.model.dashboard.BookingsRecordsResponseModel;
import com.myroom.bookingservice.api.model.dashboard.PeopleCountsResponseModel;
import com.myroom.bookingservice.data.entity.BookingDetails;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class BookingDashboardMapper {
    public PeopleCountsResponseModel toPeopleCountsResponseModel(Map<String, Long> data) {
        return PeopleCountsResponseModel.builder()
                .singleCounts(data.get("single"))
                .doubleCounts(data.get("double"))
                .othersCounts(data.get("others"))
                .build();
    }

    public BookingDataResponseModel toBookingDataResponseModel(BookingDetails booking) {
        return BookingDataResponseModel.builder()
                .id(booking.getId())
                .bookingRequestId(booking.getBookingRequestId())
                .amount(booking.getAmount())
                .checkIn(booking.getCheckIn().toString())
                .checkOut(booking.getCheckOut().toString())
                .roomDetails(booking.getRoomDetails())
                .paymentMetaDataModel(booking.getPaymentMetaDataModel())
                .status(booking.getStatus())
                .guests(booking.getGuests())
                .contactDetails(booking.getContactDetails())
                .paymentType(booking.getPaymentType())
                .bookingDate(booking.getBookingDate().toString())
                .uid(booking.getUid())
                .createdAt(booking.getCreatedAt().toString())
                .updatedAt(booking.getUpdatedAt().toString())
                .build();
    }

    public BookingsRecordsResponseModel toBookingsRecordsResponseModel(int size, List<BookingDataResponseModel> bookingsRecord) {
        return BookingsRecordsResponseModel.builder()
                .total(size)
                .data(bookingsRecord)
                .build();
    }
}