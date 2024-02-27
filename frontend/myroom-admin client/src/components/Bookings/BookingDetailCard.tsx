"use client";
import { bookings } from "@/typings";
import { formatShortDate } from "@/utils/utils";
import { UserOutlined } from "@ant-design/icons";
import { Avatar, Descriptions, Tag, message } from "antd";
import type { DescriptionsProps } from "antd";
import { PlusOutlined } from "@ant-design/icons";
import { Button, Drawer, Select, Space } from "antd";
import { useState } from "react";
import bookingService from "@/services/myRoom/booking/bookingService";

const { Option } = Select;

export default function BookingDetailCard({
  booking,
}: {
  booking: bookings.IBooking;
}) {
  const [open, setOpen] = useState(false);

  const showDrawer = () => {
    setOpen(true);
  };

  const onClose = () => {
    setOpen(false);
  };

  const items: DescriptionsProps["items"] = [
    {
      key: "1",
      label: "Booking Id",
      children: booking.id,
    },

    {
      key: "2",
      label: "Check In",
      children: formatShortDate(new Date(booking.checkIn)),
    },

    {
      key: "3",
      label: "Check Out",
      children: formatShortDate(new Date(booking.checkOut)),
    },

    {
      key: "4",
      label: "Contact Details",
      children: (
        <div
          style={{
            display: "flex",
            flexDirection: "row",
            gap: "5px",
            fontSize: "10px",
            margin: "10px",
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
            <span>{booking.contactDetails.emailId}</span>
            <span>{booking.contactDetails.fullName}</span>
          </div>
        </div>
      ),
    },

    {
      key: "4",
      label: "Room Details",
      children: (
        <div
          style={{
            display: "flex",
            flexDirection: "column",
            gap: "5px",
            margin: "10px",
          }}
        >
          <div
            style={{
              display: "flex",
              flexDirection: "row",
              gap: "5px",
            }}
          >
            <span>Room Id:</span>
            <span>{booking.roomDetails.id}</span>
          </div>

          <div
            style={{
              display: "flex",
              flexDirection: "row",
              gap: "5px",
            }}
          >
            <span>Room Title:</span>
            <span>{booking.roomDetails.title}</span>
          </div>
        </div>
      ),
    },

    {
      key: "2",
      label: "Booking RequestId Id",
      children: booking.bookingRequestId,
    },

    {
      key: "3",
      label: "amount",
      children: <Tag color="#f50">{booking.amount}</Tag>,
    },

    {
      key: "4",
      label: "status",
      children: (
        <div style={{ display: "flex", flexDirection: "column", gap: "10px" }}>
          <Tag color="blue">{booking.status}</Tag>

          <Button type="primary" onClick={showDrawer} icon={<PlusOutlined />}>
            Update Status
          </Button>
        </div>
      ),
    },

    {
      key: "5",
      label: "Payment Type",
      children: <Tag>{booking.paymentType}</Tag>,
    },

    {
      key: "5",
      label: "Booking Date",
      children: <span>{formatShortDate(new Date(booking.bookingDate))}</span>,
    },
  ];

  return (
    <div>
      <Descriptions title="Booking Info" layout="vertical" items={items} />
      <BookingDataForm open={open} onClose={onClose} booking={booking} />
    </div>
  );
}

function BookingDataForm({
  open,
  onClose,
  booking,
}: {
  open: boolean;
  onClose: () => void;
  booking: bookings.IBooking;
}): JSX.Element {
  const [messageApi, contextHolder] = message.useMessage();
  const key = "updatable";

  const [status, setStatus] = useState<string>(booking.status);

  const updateBooking = () => {
    messageApi.open({
      key,
      type: "loading",
      content: "Loading...",
    });

    bookingService
      .updateBooking({
        bookingId: booking.id,
        status: status,
      })
      .then((data) => {
        setTimeout(() => {
          messageApi.open({
            key,
            type: "success",
            content: "Loaded!",
            duration: 2,
          });

          location.reload();
        }, 1000);
      })
      .catch((error) => {
        setTimeout(() => {
          messageApi.open({
            key,
            type: "error",
            content: error.message,
            duration: 2,
          });
        }, 1000);
      });
  };

  return (
    <div>
      <Drawer
        title="Update Booking"
        width={720}
        onClose={onClose}
        open={open}
        styles={{
          body: {
            paddingBottom: 80,
          },
        }}
        extra={
          <Space>
            <Button onClick={onClose}>Cancel</Button>
            <Button onClick={updateBooking} type="primary">
              Update
            </Button>
          </Space>
        }
      >
        <div
          style={{
            width: "50%",
            display: "flex",
            flexDirection: "column",
            gap: "10px",
          }}
        >
          <strong>Status</strong>
          <Select
            placeholder="status"
            value={status}
            onSelect={(data) => {
              setStatus(data);
            }}
          >
            <Option value="PENDING_PAYMENT">PENDING_PAYMENT</Option>
            <Option value="PAY_AT_HOTEL">PAY_AT_HOTEL</Option>
            <Option value="CONFIRMED">CONFIRMED</Option>
            <Option value="CHECKED_IN">CHECKED_IN</Option>
            <Option value="CHECKED_OUT">CHECKED_OUT</Option>
            <Option value="NO_SHOW">NO_SHOW</Option>
            <Option value="CANCELLED">CANCELLED</Option>
            <Option value="EXPIRED">EXPIRED</Option>
          </Select>
        </div>
      </Drawer>
      {contextHolder}
    </div>
  );
}
