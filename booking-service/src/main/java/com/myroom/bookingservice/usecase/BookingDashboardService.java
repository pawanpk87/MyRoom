package com.myroom.bookingservice.usecase;

import com.myroom.bookingservice.api.model.dashboard.BookingsCountStatisticsResponseModel;
import com.myroom.bookingservice.api.model.dashboard.BookingsRecordsResponseModel;
import com.myroom.bookingservice.api.model.dashboard.BookingsStatisticsResponseModel;
import com.myroom.bookingservice.api.model.dashboard.PeopleCountsResponseModel;

public interface BookingDashboardService {
    BookingsStatisticsResponseModel getBookingStatistics(String organizationId, String startDate, String endDate);

    BookingsCountStatisticsResponseModel handleGetAllBookings(String organizationId, String startDate, String endDate);

    PeopleCountsResponseModel handleGetPeopleCounts(String organizationId);

    BookingsRecordsResponseModel handleGetBookingRecords(String organizationId, String startDate, String endDate);
}
