package com.myroom.bookingservice.usecase.impl;

import com.myroom.bookingservice.api.model.BookingDataResponseModel;
import com.myroom.bookingservice.api.model.dashboard.BookingsCountStatisticsResponseModel;
import com.myroom.bookingservice.api.model.dashboard.BookingsRecordsResponseModel;
import com.myroom.bookingservice.api.model.dashboard.BookingsStatisticsResponseModel;
import com.myroom.bookingservice.api.model.dashboard.PeopleCountsResponseModel;
import com.myroom.bookingservice.data.dto.PeopleCountResult;
import com.myroom.bookingservice.data.entity.BookingDetails;
import com.myroom.bookingservice.data.mapper.BookingDashboardMapper;
import com.myroom.bookingservice.repository.BookingRepository;
import com.myroom.bookingservice.usecase.BookingDashboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class BookingDashboardServiceImpl implements BookingDashboardService {
    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    BookingDashboardMapper bookingDashboardMapper;


    @Override
    public BookingsStatisticsResponseModel getBookingStatistics(String organizationId, String startDate, String endDate) {
        log.info("Fetching the bookings statistics");
        Long total = bookingRepository.countBookingsByOrganizationIdAndDateRange(organizationId, Instant.parse(startDate), Instant.parse(endDate));
        List<Object[]> data = bookingRepository.countBookingsByOrganizationIdAndDateRangeGroupByDate(organizationId, Instant.parse(startDate), Instant.parse(endDate));
        Long totalBookings = bookingRepository.getTotalBookingByOrganizationId(organizationId);

        Map<LocalDate, Long> resultMap = new HashMap<>();
        for (Object[] row : data) {
            LocalDate date = ((Date) row[0]).toLocalDate();
            Long count = (Long) row[1];
            resultMap.put(date, count);
        }

        LocalDate currentDate = Instant.parse(startDate).atZone(ZoneId.systemDefault()).toLocalDate();
        List<Long> counts = new ArrayList<>();
        while (!currentDate.isAfter(Instant.parse(endDate).atZone(ZoneId.systemDefault()).toLocalDate())) {
            counts.add(resultMap.getOrDefault(currentDate, 0L));
            currentDate = currentDate.plusDays(1);
        }

        BookingsStatisticsResponseModel bookingsStatisticsResponseModel = BookingsStatisticsResponseModel.builder()
                .totalBookings(totalBookings)
                .total(total)
                .data(counts)
                .build();

        log.info("Fetched booking statistics: {}", bookingsStatisticsResponseModel);
        return bookingsStatisticsResponseModel;
    }

    @Override
    public BookingsCountStatisticsResponseModel handleGetAllBookings(String organizationId, String startDate, String endDate) {
        log.info("Fetching the booking count");
        Long checkIn = bookingRepository.countBookingsByOrganizationIdAndCheckInDateRange(organizationId, Instant.parse(startDate), Instant.parse(endDate));
        Long checkOut = bookingRepository.countBookingsByOrganizationIdAndCheckOutDateRange(organizationId, Instant.parse(startDate), Instant.parse(endDate));

        BookingsCountStatisticsResponseModel BookingsCountStatisticsResponseModel = com.myroom.bookingservice.api.model.dashboard.BookingsCountStatisticsResponseModel.builder()
                .checkIn(checkIn)
                .checkOut(checkOut)
                .build();

        log.info("Fetched booking count: {}", BookingsCountStatisticsResponseModel);
        return BookingsCountStatisticsResponseModel;
    }

    @Override
    public PeopleCountsResponseModel handleGetPeopleCounts(String organizationId) {
        log.info("Fetching the people counts");
        Map<String, Long> data = bookingRepository.countUniqueByPeopleCountByOrganizationId(organizationId);
        log.info("Fetched people counts: {}", data);
        return bookingDashboardMapper.toPeopleCountsResponseModel(data);
    }

    @Override
    public BookingsRecordsResponseModel handleGetBookingRecords(String organizationId, String startDate, String endDate) {
        log.info("Fetching the booking records");
        List<BookingDetails> bookings = bookingRepository.findByOrganizationIdAndCreatedAtBetween(organizationId, Instant.parse(startDate), Instant.parse(endDate).plus(1, ChronoUnit.DAYS));
        List<BookingDataResponseModel> bookingsRecord =
                bookings.stream().map(booking-> bookingDashboardMapper.toBookingDataResponseModel(booking)).collect(Collectors.toList());
        return bookingDashboardMapper.toBookingsRecordsResponseModel(bookings.size(), bookingsRecord);
    }
}
