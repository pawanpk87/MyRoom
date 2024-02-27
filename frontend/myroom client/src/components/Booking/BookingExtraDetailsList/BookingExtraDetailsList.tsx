"use client";
import { Collapse, Space } from "antd";
import React from "react";
import RoomDetailsCard from "../RoomDetailsCard/RoomDetailsCard";
import { bookings } from "@/typings";
import PaymentDetailsCard from "../PaymentDetailsCard/PaymentDetailsCard";

export default function BookingExtraDetailsList({
  booking,
}: {
  booking: bookings.IBookingData;
}) {
  return (
    <Space direction="vertical">
      <Collapse
        collapsible="header"
        items={[
          {
            key: "1",
            label: "Room Details",
            children: (
              <RoomDetailsCard
                roomMetaData={booking.roomDetails}
                totalAmount={booking.amount}
              />
            ),
          },
        ]}
      />
      {booking.paymentType === bookings.PaymentType.ONLINE_PAYMENT ? (
        <Collapse
          collapsible="icon"
          items={[
            {
              key: "1",
              label: "Payment Details",
              children: <PaymentDetailsCard bookingId={booking.id} />,
            },
          ]}
        />
      ) : null}
    </Space>
  );
}
