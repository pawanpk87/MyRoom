package com.myroom.bookingservice.usecase;

import com.myroom.bookingservice.api.constants.AppConstants;
import com.myroom.bookingservice.api.model.BookingRequestRequestModel;
import com.myroom.bookingservice.api.model.UpdateBookingRequestModel;
import com.myroom.bookingservice.api.model.UpdateContactDetailsRequestModel;
import com.myroom.bookingservice.api.model.UpdatePaymentTypeRequestModel;
import com.myroom.bookingservice.data.dto.BookingRequestModelDto;

public interface BookingRequestValidationService {
    BookingRequestModelDto validateBookingRequestModelAndGetBookingRequestData(BookingRequestRequestModel bookingRequestModel);

    AppConstants.GenericValidInvalidEnum validateUpdateContactDetails(UpdateContactDetailsRequestModel updateContactDetailsRequestModel);

    AppConstants.GenericValidInvalidEnum validateUpdatePaymentType(UpdatePaymentTypeRequestModel updatePaymentTypeRequestModel);

    AppConstants.GenericValidInvalidEnum validateUpdateBookingRequest(UpdateBookingRequestModel updateBookingRequestModel);

    AppConstants.GenericValidInvalidEnum validateBookingRequestData(BookingRequestRequestModel bookingRequestModel);
}