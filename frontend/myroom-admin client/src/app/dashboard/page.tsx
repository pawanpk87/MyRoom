"use client";
import TotalBooking from "@/components/Dashboard/StatisticCards/TotalBooking/TotalBooking";
import TotalRevenue from "@/components/Dashboard/StatisticCards/TotalRevenue/TotalRevenue";
import TotalCheckIn from "@/components/Dashboard/Cards/TotalCheckIn/TotalCheckIn";
import NewBookings from "@/components/Dashboard/Cards/NewBookings/NewBookings";
import IncomeChart from "@/components/Dashboard/StatisticCharts/IncomeChart/IncomeChart";
import RoomBookingChart from "@/components/Dashboard/StatisticCharts/RoomBookingChart/RoomBookingChart";
import TopBookedRoom from "@/components/Dashboard/StatisticCharts/TopBookedRoom/TopBookedRoom";
import RecentBookings from "@/components/Dashboard/RecentBookings/RecentBookings";
import { useUserAuth } from "@/firebase/auth/authProvider";
import { message } from "antd";
import React, { useEffect, useState } from "react";
import { bookings, organizations } from "@/typings";
import bookingService from "@/services/myRoom/booking/bookingService";
import "./dashboard.css";
import TotalCheckOut from "@/components/Dashboard/Cards/TotalCheckOut/TotalCheckOut";
import RoomAvailable from "@/components/Dashboard/Cards/RoomAvailable/RoomAvailable";

export default function Dashboard() {
  const { user, organization } = useUserAuth();

  const [messageApi, contextHolder] = message.useMessage();

  const success = (message: string) => {
    messageApi.open({
      type: "success",
      content: message,
    });
  };

  const error = (message: string) => {
    messageApi.open({
      type: "error",
      content: message,
    });
  };

  const warning = (message: string) => {
    messageApi.open({
      type: "warning",
      content: message,
    });
  };

  return user && organization !== null ? (
    <div className="dashboard">
      <div className="statisticCards">
        <TotalBooking organization={organization} error={error} />

        <TotalRevenue organization={organization} error={error} />

        <CheckInAndCheckOut organization={organization} error={error} />
      </div>

      <div style={{ display: "flex", flexDirection: "row", gap: "10px" }}>
        <IncomeChart organization={organization} error={error} />
        <div style={{ display: "flex", flexDirection: "column", gap: "10px" }}>
          <RoomBookingChart organization={organization} error={error} />
          <TopBookedRoom organization={organization} error={error} />
        </div>
      </div>

      <div>
        <RecentBookings organization={organization} error={error} />
      </div>

      {contextHolder}
    </div>
  ) : null;
}

function CheckInAndCheckOut({
  organization,
  error,
}: {
  organization: organizations.IOrganization;
  error: (message: string) => void;
}) {
  const [todayData, setTodayData] =
    React.useState<bookings.IGetBookingCount | null>(null);

  useEffect(() => {
    const currDate = new Date();

    const queryForTodayData: bookings.IGetBookingStatisticQuery = {
      organizationId: organization.id,
      startDate: currDate.toISOString(),
      endDate: currDate.toISOString(),
    };

    bookingService
      .getBookingsCount(queryForTodayData)
      .then((data) => {
        setTodayData(data.data);
      })
      .catch((err) => {
        console.log(error);
        error(err.message);
      });
  }, [organization.id]);

  const [data, setDate] = useState<bookings.IGetBookingCount | null>(null);

  useEffect(() => {
    if (todayData) {
      const finalDate: bookings.IGetBookingCount = {
        checkIn: todayData.checkIn,
        checkOut: todayData.checkOut,
      };
      setDate(finalDate);
    }
  }, [todayData]);

  return (
    data && (
      <>
        <div style={{ display: "flex", flexDirection: "column", gap: "5px" }}>
          <TotalCheckIn count={data.checkIn} />
          <TotalCheckOut count={data.checkOut} />
        </div>
        <div style={{ display: "flex", flexDirection: "column", gap: "5px" }}>
          <RoomAvailable organization={organization} error={error} />
        </div>
      </>
    )
  );
}
