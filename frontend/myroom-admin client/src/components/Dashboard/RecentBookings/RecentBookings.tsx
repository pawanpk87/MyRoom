"use client";
import { Avatar, Space, Table, Tag } from "antd";
import type { TableColumnsType, TableProps } from "antd";
import "./RecentBookings.css";
import { bookings, organizations } from "@/typings";
import bookingService from "@/services/myRoom/booking/bookingService";
import { useEffect, useState } from "react";
import { UserOutlined } from "@ant-design/icons";
import { formatShortDate } from "@/utils/utils";

interface DataType extends bookings.IBooking {}

const columns: TableColumnsType<DataType> = [
  {
    title: "Id",
    dataIndex: "id",
    key: "id",
    width: "100px",
    ellipsis: true,
  },

  {
    title: "Guest",
    dataIndex: "contactDetails",
    key: "contactDetails",
    width: "200px",
    ellipsis: true,
    render: (contactDetails) => {
      const details = contactDetails;
      return (
        <div
          style={{
            display: "flex",
            flexDirection: "row",
            gap: "5px",
            fontSize: "10px",
          }}
        >
          <Avatar
            style={{ backgroundColor: "#87d068" }}
            icon={<UserOutlined />}
          />

          <div
            style={{
              display: "flex",
              flexDirection: "column",
            }}
          >
            <span>{details.emailId}</span>
            <span>{details.fullName}</span>
          </div>
        </div>
      );
    },
  },

  {
    title: "Booking Date",
    dataIndex: "bookingDate",
    key: "bookingDate",
    render: (value: string) => formatShortDate(new Date(value)),
    sorter: (a, b) =>
      new Date(a.bookingDate).valueOf() - new Date(b.bookingDate).valueOf(),
  },

  {
    title: "Check In",
    dataIndex: "checkIn",
    key: "checkIn",
    render: (value: string) => formatShortDate(new Date(value)),
    sorter: (a, b) =>
      new Date(a.checkIn).valueOf() - new Date(b.checkIn).valueOf(),
  },

  {
    title: "Check Out",
    dataIndex: "checkOut",
    key: "checkOut",
    render: (value: string) => formatShortDate(new Date(value)),
    sorter: (a, b) =>
      new Date(a.checkOut).valueOf() - new Date(b.checkOut).valueOf(),
  },

  {
    title: "Status",
    key: "status",
    dataIndex: "status",
    width: "150px",
    filters: [
      {
        text: "Confirmed",
        value: bookings.BookingStatus.CONFIRMED,
      },

      {
        text: "Pending Payment",
        value: bookings.BookingStatus.PENDING_PAYMENT,
      },

      {
        text: "Pay at hotel",
        value: bookings.BookingStatus.PAY_AT_HOTEL,
      },

      {
        text: "CheckIn",
        value: bookings.BookingStatus.CHECKED_IN,
      },

      {
        text: "CheckOut",
        value: bookings.BookingStatus.CHECKED_OUT,
      },

      {
        text: "Cancelled",
        value: bookings.BookingStatus.CANCELLED,
      },

      {
        text: "Expired",
        value: bookings.BookingStatus.EXPIRED,
      },

      {
        text: "No Show",
        value: bookings.BookingStatus.NO_SHOW,
      },
    ],

    onFilter(value, record) {
      return record.status === value;
    },

    render: (_, data, index) => {
      switch (data.status) {
        case bookings.BookingStatus.CONFIRMED:
          return <Tag color="green">Confirmed</Tag>;

        case bookings.BookingStatus.PENDING_PAYMENT:
          return <Tag color="volcano">Pending Payment</Tag>;

        case bookings.BookingStatus.PAY_AT_HOTEL:
          return <Tag color="magenta">Pay at hotel</Tag>;

        case bookings.BookingStatus.CHECKED_IN:
          return <Tag color="blue">CheckIn</Tag>;

        case bookings.BookingStatus.CHECKED_OUT:
          return <Tag color="geekblue">CheckOut</Tag>;

        case bookings.BookingStatus.CANCELLED:
          return <Tag color="gold">Cancelled</Tag>;

        case bookings.BookingStatus.EXPIRED:
          return <Tag color="magenta">Expired</Tag>;

        case bookings.BookingStatus.NO_SHOW:
          return <Tag color="cyan">No Show</Tag>;
      }
    },
  },
];

export default function RecentBookings({
  organization,
  error,
}: {
  organization: organizations.IOrganization;
  error: (message: string) => void;
}) {
  const [data, setData] = useState<DataType[] | null>(null);

  useEffect(() => {
    const currDate = new Date();

    const query: bookings.IGetBookingsRecordsQuery = {
      organizationId: organization.id,
      startDate: new Date(
        currDate.setDate(currDate.getDate() - 1)
      ).toISOString(),
      endDate: currDate.toISOString(),
    };

    bookingService
      .getBookingRecords(query)
      .then((data) => {
        setData(data.data);
        const bookingsRecords: bookings.IBooking[] = data.data.data;
        setData(bookingsRecords);
      })
      .catch((err) => {
        console.log(error);
        error(err.message);
      });
  }, []);

  return (
    data && (
      <div className="recentBookings">
        <div className="header">
          <strong>Recent Bookings</strong>
        </div>
        <div className="bookings">
          <Table
            columns={columns}
            dataSource={data}
            pagination={false}
            scroll={{ x: 400, y: 300 }}
          />
        </div>
      </div>
    )
  );
}
