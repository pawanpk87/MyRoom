package com.myroom.paymentservice.util;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;

public class DateRangeCalculator {
    public static DateRange getDateRange(String interval) {
        Instant currentDate = Instant.now();
        Instant startDate, endDate, previousStartDate, previousEndDate;

        switch (interval) {
            case "week":
                Instant startOfWeek = currentDate.atZone(ZoneId.systemDefault())
                        .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                        .toInstant();
                startDate = startOfWeek;
                endDate = currentDate;
                previousStartDate = startOfWeek.minus(java.time.Duration.ofDays(7));
                previousEndDate = startOfWeek.minus(java.time.Duration.ofDays(1));
                break;
            case "month":
                Instant startOfMonth = currentDate.atZone(ZoneId.systemDefault())
                        .with(TemporalAdjusters.firstDayOfMonth())
                        .toInstant();
                startDate = startOfMonth;
                endDate = currentDate;
                previousStartDate = startOfMonth.minus(java.time.Duration.ofDays(1)).with(TemporalAdjusters.firstDayOfMonth());
                previousEndDate = startOfMonth.minus(java.time.Duration.ofDays(1));
                break;
            case "year":
                Instant startOfYear = currentDate.atZone(ZoneId.systemDefault())
                        .with(TemporalAdjusters.firstDayOfYear())
                        .toInstant();
                startDate = startOfYear;
                endDate = currentDate;
                previousStartDate = startOfYear.minus(java.time.Duration.ofDays(1)).with(TemporalAdjusters.firstDayOfYear());
                previousEndDate = startOfYear.minus(java.time.Duration.ofDays(1));
                break;
            default:
                throw new IllegalArgumentException("Invalid interval");
        }

        return new DateRange(startDate, endDate, previousStartDate, previousEndDate);
    }

    public static void main(String[] args) {
        DateRange dateRange = getDateRange("week");
        System.out.println(dateRange);
    }
}

