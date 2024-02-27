"use client";
import React, { useEffect, useState } from "react";
import StatisticCard, {
  StatisticCardChartType,
  StatisticCardProps,
} from "../StatisticCard/StatisticCard";
import { bookings, organizations } from "@/typings";
import bookingService from "@/services/myRoom/booking/bookingService";

function TotalBooking({
  organization,
  error,
}: {
  organization: organizations.IOrganization;
  error: (message: string) => void;
}) {
  const [monthData, setMonthDate] =
    React.useState<bookings.IBookingsStatistic | null>(null);
  const [weekData, setWeekDate] =
    React.useState<bookings.IBookingsStatistic | null>(null);

  useEffect(() => {
    const currDate = new Date();

    const queryForMonthData: bookings.IGetBookingStatisticQuery = {
      organizationId: organization.id,
      startDate: new Date(
        currDate.getFullYear(),
        currDate.getMonth(),
        1
      ).toISOString(),
      endDate: currDate.toISOString(),
    };

    const queryForWeekData: bookings.IGetBookingStatisticQuery = {
      organizationId: organization.id,
      startDate: new Date(
        currDate.setDate(currDate.getDate() - 7)
      ).toISOString(),
      endDate: new Date().toISOString(),
    };

    bookingService
      .getBookingStatistic(queryForMonthData)
      .then((data) => {
        setMonthDate(data.data);
      })
      .catch((err) => {
        console.log(error);
        error(err.message);
      });

    bookingService
      .getBookingStatistic(queryForWeekData)
      .then((data) => {
        setWeekDate(data.data);
      })
      .catch((err) => {
        console.log(error);
        error(err.message);
      });
  }, [organization.id]);

  const [data, setDate] = useState<any>(null);

  useEffect(() => {
    if (monthData && weekData) {
      const myBookings = weekData.data.map((value, index) => {
        return {
          name: "Booking",
          Booking: value,
        };
      });

      const finalDate: StatisticCardProps = {
        title: "Total Booking",
        key: "Booking",
        total: weekData.totalBookings,
        thisMonth: monthData.total,
        thisWeek: weekData.total,
        data: myBookings,
        chartType: StatisticCardChartType.BAR,
      };

      setDate(finalDate);
    }
  }, [monthData, weekData]);

  return data ? <StatisticCard data={data} /> : null;
}

export default TotalBooking;
