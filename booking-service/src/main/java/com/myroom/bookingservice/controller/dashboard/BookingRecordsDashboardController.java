package com.myroom.bookingservice.controller.dashboard;

import com.myroom.bookingservice.api.model.dashboard.BookingsCountStatisticsResponseModel;
import com.myroom.bookingservice.api.model.dashboard.BookingsRecordsResponseModel;
import com.myroom.bookingservice.api.resource.dashboard.BookingRecordsDashboardResource;
import com.myroom.bookingservice.usecase.BookingDashboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class BookingRecordsDashboardController implements BookingRecordsDashboardResource {
    @Autowired
    BookingDashboardService bookingDashboardService;

    @Override
    public ResponseEntity<BookingsRecordsResponseModel> handleGetBookingRecords(String organizationId, String startDate, String endDate) {
        log.info("Fetching booking records for organizationId: {}", organizationId);
        BookingsRecordsResponseModel bookingRecords = bookingDashboardService.handleGetBookingRecords(organizationId, startDate, endDate);
        log.info("Fetched the booking records: {}", bookingRecords);
        return new ResponseEntity<>(bookingRecords, HttpStatus.OK);
    }
}