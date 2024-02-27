package com.myroom.bookingservice.controller;

import com.myroom.bookingservice.api.model.*;
import com.myroom.bookingservice.api.model.query.GetAllBookingsQuery;
import com.myroom.bookingservice.api.resource.BookingResource;
import com.myroom.bookingservice.data.entity.BookingDetails;
import com.myroom.bookingservice.usecase.BookingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;

@Controller
@Slf4j
public class BookingController implements BookingResource {
    @Autowired
    BookingService bookingService;


    @Override
    public ResponseEntity<BookingOrderResponseModel> createBooking(String bookingRequestId, BookingOrderRequestModel bookingOrderRequestModel) {
        log.info("Creating booking order for bookingRequestId: {}", bookingRequestId);
        BookingOrderResponseModel bookingOrderResponseModel = bookingService.createBookingOrder(bookingRequestId, bookingOrderRequestModel);
        log.info("Created Booking order: {}", bookingOrderResponseModel);
        return new ResponseEntity<>(bookingOrderResponseModel, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<BookingDataResponseModel> updateBooking(String bookingId, UpdateBookingOrderRequestModel updateBookingOrderRequestModel) {
        log.info("Updating booking details for bookingId: {}", bookingId);
        BookingDataResponseModel bookingDataResponseModel = bookingService.updateBooking(bookingId, updateBookingOrderRequestModel);
        log.info("Updated the booking: {}", bookingDataResponseModel);
        return new ResponseEntity<>(bookingDataResponseModel, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Page<BookingDetails>> getBookings(GetAllBookingsQuery query) {
        log.info("Fetching booking data");
        Page<BookingDetails> bookings = bookingService.getBookings(query);
        log.info("Fetched the booking data:{}", bookings);
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BookingDataResponseModel> getBookingDataByBookingRequestId(String bookingRequestId) {
        log.info("Fetching booking data for booking request id: {}", bookingRequestId);
        BookingDataResponseModel bookingDataResponseModel = bookingService.getBookingDataByBookingRequestId(bookingRequestId);
        log.info("Fetched the booking data:{}", bookingDataResponseModel);
        return new ResponseEntity<>(bookingDataResponseModel, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BookingDataResponseModel> getBookingData(String bookingId) {
        log.info("Fetching booking data for booking id: {}", bookingId);
        BookingDataResponseModel bookingDataResponseModel = bookingService.getBookingDataById(bookingId);
        log.info("Fetched the booking data:{}", bookingDataResponseModel);
        return new ResponseEntity<>(bookingDataResponseModel, HttpStatus.OK);
    }

    @Override
    public RedirectView handleBookingPaymentSuccess(String bookingId) {
        log.info("Received booking payment success request");
        return new RedirectView("http://localhost:3000/bookings/"+bookingId);
    }

    @Override
    public RedirectView handleBookingPaymentCancel(String bookingId) {
        log.info("Received booking payment cancel request");
        bookingService.handleBookingPaymentCancel(bookingId);
        return new RedirectView("http://localhost:3000");
    }
}