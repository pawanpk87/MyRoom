"use client";
import reviewService from "@/services/myRoom/review/reviewService";
import { rooms } from "@/typings";
import { reviews } from "@/typings/reviews";
import React, { useEffect } from "react";
import OverallRatingBox from "../OverallRatingBox/OverallRatingBox";
import ReviewListItem from "../ReviewListItem/ReviewListItem";
import "./RoomReviews.css";

export default function RoomReviews({
  room,
}: {
  room: rooms.IRoomData;
}): JSX.Element | null {
  const [ratingData, setReivew] = React.useState<reviews.IOverallRating | null>(
    null
  );

  useEffect(() => {
    reviewService
      .getOverallRating(room.id)
      .then((data) => {
        setReivew(data.data);
      })
      .catch((error) => {
        console.log(error);
      });
  }, [room.id]);

  return ratingData && ratingData.overall !== null ? (
    <div className="description_card">
      <div className="description_title">
        <h2>Ratings and reviews</h2>
      </div>
      <div>
        <div className="rating_reviews">
          <OverallRatingBox ratingData={ratingData} />
          <div>
            <ReviewListItem roomId={room.id} />
          </div>
        </div>
      </div>
    </div>
  ) : null;
}
