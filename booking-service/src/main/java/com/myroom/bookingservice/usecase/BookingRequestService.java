package com.myroom.bookingservice.usecase;

import com.myroom.bookingservice.api.constants.BookingRequestStatus;
import com.myroom.bookingservice.api.model.*;
import com.myroom.bookingservice.data.entity.BookingRequestDetails;

public interface BookingRequestService {
    BookingRequestResponseModel createBookingRequest(BookingRequestRequestModel bookingRequestModel);

    BookingRequestResponseModel updateContactDetails(String bookingRequestId, UpdateContactDetailsRequestModel updateContactDetailsRequestModel);

    BookingRequestResponseModel updatePaymentType(String bookingRequestId, UpdatePaymentTypeRequestModel updatePaymentTypeRequestModel);

    void updateStatus(String bookingRequestId, BookingRequestStatus status);

    BookingRequestResponseModel updateBookingRequest(String bookingRequestId, UpdateBookingRequestModel updateBookingRequestModel);

    BookingRequestResponseModel getBookingRequest(String bookingRequestId);

    BookingRequestDetails getBookingRequestData(String bookingRequestId);

    CancelBookingRequestResponseModel cancelBookingRequest(String bookingRequestId, CancelBookingRequestModel cancelBookingRequestModel);
}