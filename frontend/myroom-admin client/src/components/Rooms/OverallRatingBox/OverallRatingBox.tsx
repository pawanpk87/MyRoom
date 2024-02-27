"use client";
import { Progress } from "antd";
import RatingBox from "../RatingBox/RatingBox";
import React from "react";
import { calculatePercentage } from "@/utils/utils";
import { reviews } from "@/typings";
import "./OverallRatingBox.css";

export default function OverallRatingBox({
  ratingData,
}: {
  ratingData: reviews.IOverallRating;
}) {
  return (
    <div className="overallRatingBox">
      <div className="overall">
        <RatingBox rating={ratingData.overall} style={{ fontSize: "large" }} />
      </div>
      <div className="statistics">
        <Rating
          type={5}
          rating={
            ratingData.statistics["5star"] ? ratingData.statistics["5star"] : 0
          }
          overall={ratingData.overall}
        />
        <Rating
          type={4}
          rating={
            ratingData.statistics["4star"] ? ratingData.statistics["4star"] : 0
          }
          overall={ratingData.overall}
        />

        <Rating
          type={3}
          rating={
            ratingData.statistics["3star"] ? ratingData.statistics["3star"] : 0
          }
          overall={ratingData.overall}
        />

        <Rating
          type={2}
          rating={
            ratingData.statistics["2star"] ? ratingData.statistics["2star"] : 0
          }
          overall={ratingData.overall}
        />

        <Rating
          type={1}
          rating={
            ratingData.statistics["1star"] ? ratingData.statistics["1star"] : 0
          }
          overall={ratingData.overall}
        />
      </div>
    </div>
  );
}

function Rating({
  type,
  rating,
  overall,
}: {
  type: number;
  rating: number;
  overall: number;
}): JSX.Element {
  return (
    <div className="statisticRating">
      <div className="statisticRatingIcon">
        {type}
        <svg
          xmlns="http://www.w3.org/2000/svg"
          width="10"
          height="10"
          viewBox="0 0 10 10"
        >
          <path
            fill="#cdcdcd"
            fill-rule="evenodd"
            d="M9.495 3.42H6.423L5.473.357A.499.499 0 0 0 5.003 0a.499.499 0 0 0-.472.357L3.568 3.42H.495a.499.499 0 0 0-.47.358.533.533 0 0 0 .177.58L2.7 6.252 1.744 9.32a.54.54 0 0 0 .07.466.49.49 0 0 0 .401.214.496.496 0 0 0 .282-.105l2.498-1.894 2.498 1.894a.495.495 0 0 0 .29.101.49.49 0 0 0 .4-.214.54.54 0 0 0 .07-.466L7.305 6.25l2.498-1.895a.534.534 0 0 0 .171-.583.498.498 0 0 0-.478-.35z"
          ></path>
        </svg>
      </div>
      <div className="progress">
        <Progress percent={calculatePercentage(rating, overall)} />
      </div>
    </div>
  );
}
