"use client";
import { bookings } from "@/typings";
import { Steps } from "antd";
import React from "react";

export default function BookingStatus({
  status,
}: {
  status: bookings.BookingStatus;
}) {
  let bookingViaOnlinePayItems: any = [
    {
      title: "Payment",
      description: null,
    },
    {
      title: "Booking Confirmed",
      description: null,
    },
    {
      title: "Checked-In",
      description: null,
    },
    {
      title: "CheckedOut",
      description: null,
    },
  ];

  let current = 0;

  switch (status) {
    case bookings.BookingStatus.PENDING_PAYMENT:
      current = 0;
      break;

    case bookings.BookingStatus.CONFIRMED:
    case bookings.BookingStatus.PAY_AT_HOTEL:
      current = 1;
      break;

    case bookings.BookingStatus.CHECKED_IN:
      current = 2;
      break;

    case bookings.BookingStatus.CHECKED_OUT:
      current = 3;
      break;
  }

  return (
    <Steps
      direction="vertical"
      size="small"
      current={current}
      items={bookingViaOnlinePayItems}
    />
  );
}
