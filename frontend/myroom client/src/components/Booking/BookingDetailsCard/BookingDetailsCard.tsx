"use client";
import { bookings, rooms } from "@/typings";
import { formatShortDate } from "@/utils/utils";
import {
  Collapse,
  Descriptions,
  DescriptionsProps,
  Input,
  Rate,
  Tag,
  message,
} from "antd";
import React, { useState } from "react";
import { Button, Modal } from "antd";
import roomService from "@/services/myRoom/rooms/roomService";

export default function BookingDetailsCard({
  booking,
}: {
  booking: bookings.IBookingData;
}) {
  const [givenRating, setGivenRating] = useState<number>(0);
  const [comment, setComment] = useState<string | null>(null);

  const [messageApi, contextHolder] = message.useMessage();

  const items: DescriptionsProps["items"] = [
    {
      key: "1",
      label: "Booking Id",
      children: booking.id,
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
      children: <Tag color="blue">{booking.status}</Tag>,
    },

    {
      key: "5",
      label: "Payment Type",
      children: <Tag>{booking.paymentType}</Tag>,
    },

    {
      key: "6",
      label: "Booking Date",
      children: <span>{formatShortDate(new Date(booking.bookingDate))}</span>,
    },
  ];

  const error = (message: string) => {
    messageApi.open({
      type: "error",
      content: message,
    });
  };
  const success = (message: string) => {
    messageApi.open({
      type: "success",
      content: message,
    });
  };

  const [isModalOpen, setIsModalOpen] = useState(false);

  const showModal = () => {
    setIsModalOpen(true);
  };

  const handleOk = () => {
    if (givenRating > 0 && (comment === null || comment.trim().length === 0)) {
      error("Please write some comment");
      return;
    }
    messageApi.open({
      type: "Sending",
      content: "Rate the booking experience",
    });

    const newReview: rooms.IAddReview = {
      roomId: booking.roomDetails.id,
      organizationId: booking.roomDetails.organizationId,
      uid: booking.uid,
      reviewText: comment!,
      rating: givenRating,
    };

    roomService
      .addReview(newReview)
      .then((data) => {
        success("success");
        setTimeout(() => {
          location.reload();
        }, 2000);
      })
      .catch((err) => {
        error(err.message);
      });
  };

  const handleCancel = () => {
    setIsModalOpen(false);
  };

  return (
    <div style={{ display: "flex", flexDirection: "column", gap: "10px" }}>
      {contextHolder}

      {booking.status === bookings.BookingStatus.CHECKED_OUT ? (
        <div>
          <Button onClick={showModal} type="primary">
            Rate the booking experience
          </Button>

          <Modal
            title="Please give rating"
            open={isModalOpen}
            onOk={handleOk}
            onCancel={handleCancel}
          >
            <div
              style={{ display: "flex", flexDirection: "column", gap: "10px" }}
            >
              <Rate onChange={(value) => setGivenRating(value)} />
              <Input
                placeholder="Your comment"
                value={comment}
                onChange={(e) => setComment(e.target.value)}
              />
            </div>
          </Modal>
        </div>
      ) : null}

      <Collapse
        collapsible="header"
        defaultActiveKey={1}
        items={[
          {
            key: "1",
            label: "Booking Details",
            children: <Descriptions title={null} items={items} />,
          },
        ]}
      />
    </div>
  );
}
