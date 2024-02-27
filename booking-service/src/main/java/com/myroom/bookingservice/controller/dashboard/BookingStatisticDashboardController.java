package com.myroom.bookingservice.controller.dashboard;

import com.myroom.bookingservice.api.model.dashboard.BookingsCountStatisticsResponseModel;
import com.myroom.bookingservice.api.model.dashboard.BookingsStatisticsResponseModel;
import com.myroom.bookingservice.api.model.dashboard.PeopleCountsResponseModel;
import com.myroom.bookingservice.api.resource.dashboard.BookingStatisticDashboardResource;
import com.myroom.bookingservice.usecase.BookingDashboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class BookingStatisticDashboardController implements BookingStatisticDashboardResource {
    @Autowired
    BookingDashboardService bookingDashboardService;

    @Override
    public ResponseEntity<BookingsStatisticsResponseModel> handleGetBookingStatistics(String organizationId, String startDate, String endDate) {
        log.info("Fetching booking statistics for organizationId: {}", organizationId);
        BookingsStatisticsResponseModel bookingsStatistics = bookingDashboardService.getBookingStatistics(organizationId, startDate, endDate);
        log.info("Fetched the booking statistics: {}", bookingsStatistics);
        return new ResponseEntity<>(bookingsStatistics, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BookingsCountStatisticsResponseModel> handleGetAllBookings(String organizationId, String startDate, String endDate) {
        log.info("Fetching booking count for organizationId: {}", organizationId);
        BookingsCountStatisticsResponseModel bookingsCount = bookingDashboardService.handleGetAllBookings(organizationId, startDate, endDate);
        log.info("Fetched the booking counts: {}", bookingsCount);
        return new ResponseEntity<>(bookingsCount, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PeopleCountsResponseModel> handleGetPeopleCounts(String organizationId) {
        log.info("Fetching people counts for organizationId: {}", organizationId);
        PeopleCountsResponseModel peopleCountsResponseModel = bookingDashboardService.handleGetPeopleCounts(organizationId);
        log.info("Fetched the people counts: {}", peopleCountsResponseModel);
        return new ResponseEntity<>(peopleCountsResponseModel, HttpStatus.OK);
    }
}