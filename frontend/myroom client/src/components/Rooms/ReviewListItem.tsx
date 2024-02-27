"use client";
import { reviews } from "@/typings/review";
import User from "../User/User";
import React, { useEffect } from "react";
import reviewService from "@/services/myRoom/reviews/reviewService";
import "./ReviewListItem.css";

export default function ReviewListItem({ roomId }: { roomId: string }) {
  const [data, setData] = React.useState<reviews.IGetReviewsData | null>(null);

  useEffect(() => {
    const query: reviews.IGetReviewsQuery = {
      roomId: roomId,
    };

    reviewService
      .getReviews(query)
      .then((data) => {
        setData(data.data);
      })
      .catch((error) => {
        console.log(error);
      });
  }, [roomId]);

  return (
    data && (
      <div className="reviewListItems">
        {data.reviews.map((data: reviews.Review) => (
          <UserReview key={data.id} data={data} />
        ))}
      </div>
    )
  );
}

function UserReview({ data }: { data: reviews.Review }): JSX.Element {
  return (
    <div className="userReview">
      <div className="user">
        <User uid={data.uid} postedOn={data.createdAt} />
      </div>
      <div>{data.reviewText}</div>
    </div>
  );
}
