"use client";
import { reviews } from "@/typings/reviews";
import { Descriptions, Rate } from "antd";
import type { DescriptionsProps } from "antd";
import User from "./User/User";

export default function ReviewDetailCard({
  review,
}: {
  review: reviews.IReviewData;
}) {
  const items: DescriptionsProps["items"] = [
    {
      key: "1",
      label: "Review Id",
      children: review.id,
    },

    {
      key: "2",
      label: "Rating",
      children: <Rate disabled defaultValue={review.rating} />,
    },

    {
      key: "3",
      label: "User",
      children: <User uid={review.uid} />,
    },

    {
      key: "4",
      label: "Review Text",
      children: review.reviewText,
    },

    {
      key: "5",
      label: "Room Id",
      children: review.roomId,
    },
  ];

  return <Descriptions title="Booking Info" layout="vertical" items={items} />;
}
