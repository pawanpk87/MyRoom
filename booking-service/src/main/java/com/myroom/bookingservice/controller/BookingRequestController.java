package com.myroom.bookingservice.controller;

import com.myroom.bookingservice.api.model.*;
import com.myroom.bookingservice.api.resource.BookingRequestResource;
import com.myroom.bookingservice.usecase.BookingRequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class BookingRequestController implements BookingRequestResource {
    @Autowired
    BookingRequestService bookingRequestService;


    @Override
    public ResponseEntity<BookingRequestResponseModel> createBookingRequest(BookingRequestRequestModel bookingRequestModel) {
        log.info("BookingRequest for: {}", bookingRequestModel);
        BookingRequestResponseModel bookingResponseModel = bookingRequestService.createBookingRequest(bookingRequestModel);
        log.info("created BookingRequest request: {}", bookingResponseModel);
        return new ResponseEntity<>(bookingResponseModel, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<BookingRequestResponseModel> updateContactDetails(String bookingRequestId, UpdateContactDetailsRequestModel updateContactDetailsRequestModel) {
        log.info("Updating BookingRequest contact Details for: {}", updateContactDetailsRequestModel);
        BookingRequestResponseModel bookingResponseModel = bookingRequestService.updateContactDetails(bookingRequestId, updateContactDetailsRequestModel);
        log.info("Updated BookingRequest: {}", bookingResponseModel);
        return new ResponseEntity<>(bookingResponseModel, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BookingRequestResponseModel> updatePaymentType(String bookingRequestId, UpdatePaymentTypeRequestModel updatePaymentTypeRequestModel) {
        log.info("Updating BookingRequest payment type for: {}", updatePaymentTypeRequestModel);
        BookingRequestResponseModel bookingResponseModel = bookingRequestService.updatePaymentType(bookingRequestId, updatePaymentTypeRequestModel);
        log.info("Updated BookingRequest: {}", bookingResponseModel);
        return new ResponseEntity<>(bookingResponseModel, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BookingRequestResponseModel> updateBookingRequest(String bookingRequestId, UpdateBookingRequestModel updateBookingRequestModel) {
        log.info("Updating BookingRequest for: {}", updateBookingRequestModel);
        BookingRequestResponseModel bookingResponseModel = bookingRequestService.updateBookingRequest(bookingRequestId, updateBookingRequestModel);
        log.info("Updated BookingRequest: {}", bookingResponseModel);
        return new ResponseEntity<>(bookingResponseModel, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BookingRequestResponseModel> getBookingRequest(String bookingRequestId) {
        log.info("Fetching BookingRequest for: {}", bookingRequestId);
        BookingRequestResponseModel bookingResponseModel = bookingRequestService.getBookingRequest(bookingRequestId);
        log.info("Fetched BookingRequest: {}", bookingResponseModel);
        return new ResponseEntity<>(bookingResponseModel, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CancelBookingRequestResponseModel> cancelBookingRequest(String bookingRequestId, CancelBookingRequestModel cancelBookingRequestModel) {
        log.info("Canceling BookingRequest for bookingRequestId: {}", bookingRequestId);
        CancelBookingRequestResponseModel cancelBookingRequestResponseModel = bookingRequestService.cancelBookingRequest(bookingRequestId, cancelBookingRequestModel);
        log.info("Canceled BookingRequest: {}", cancelBookingRequestResponseModel);
        return new ResponseEntity(cancelBookingRequestResponseModel, HttpStatus.OK);
    }
}