package com.myroom.bookingservice.usecase;


import com.myroom.bookingservice.api.model.BookingDataResponseModel;
import com.myroom.bookingservice.api.model.BookingOrderRequestModel;
import com.myroom.bookingservice.api.model.BookingOrderResponseModel;
import com.myroom.bookingservice.api.model.UpdateBookingOrderRequestModel;
import com.myroom.bookingservice.api.model.query.GetAllBookingsQuery;
import com.myroom.bookingservice.data.entity.BookingDetails;
import org.springframework.data.domain.Page;

public interface BookingService {
    BookingOrderResponseModel createBookingOrder(String bookingRequestId, BookingOrderRequestModel bookingOrderRequestModel);

    BookingDataResponseModel getBookingDataByBookingRequestId(String bookingRequestId);

    BookingDataResponseModel getBookingDataById(String id);

    void handleBookingPaymentCancel(String bookingId);

    void updateBookingStatusAndBookingPaymentMetaDataModel(String bookingId, String status);

    Page<BookingDetails> getBookings(GetAllBookingsQuery query);

    BookingDataResponseModel updateBooking(String bookingId, UpdateBookingOrderRequestModel updateBookingOrderRequestModel);
}