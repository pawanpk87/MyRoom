package com.myroom.paymentservice.util;

import lombok.Data;

import java.time.Instant;

@Data
public class DateRange {
    private Instant startDate;
    private Instant endDate;
    private Instant previousStartDate;
    private Instant previousEndDate;

    public DateRange(Instant startDate, Instant endDate, Instant previousStartDate, Instant previousEndDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.previousStartDate = previousStartDate;
        this.previousEndDate = previousEndDate;
    }

    @Override
    public String toString() {
        return "DateRange{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                ", previousStartDate=" + previousStartDate +
                ", previousEndDate=" + previousEndDate +
                '}';
    }
}
