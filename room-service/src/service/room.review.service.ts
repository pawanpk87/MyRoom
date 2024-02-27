import axios, { AxiosRequestConfig } from "axios";
import logger from "../config/logger";
import AppConstants from "../constants/AppConstants";
import { createReviewInput } from "../schema";
import {
  calculateOverallRating,
  getInstanceURI,
  handleAxiosError,
} from "../utils";
import ApiConstants from "../constants/ApiConstants";
import { ICreatedReview } from "./types";
import RoomModel from "../model/room.model";
import { mapKeys, omit } from "lodash";

export async function createReview(input: createReviewInput): Promise<any> {
  logger.info(`Attempting review creation with input: `, input);

  const reviewData: ICreatedReview = await insertNewReview(input);

  const overallRating: number = calculateOverallRating(
    reviewData.sumOfRatings,
    reviewData.totalReviewCount
  );

  logger.info(`Updating overall rating of room with roomId: ${input.roomId}`);
  const roomData = await RoomModel.findByIdAndUpdate(
    input.roomId,
    {
      $set: {
        "rating.rating": overallRating,
      },
    },
    { new: true }
  );

  logger.info(`Overall rating updated successfully: `, roomData);

  const formattedRoomData = mapKeys(
    omit(JSON.parse(JSON.stringify(roomData)), [
      "__v",
      "createdAt",
      "updatedAt",
    ]),
    (value, key) => {
      return key === "_id" ? "id" : key;
    }
  );

  return {
    success: true,
    review: reviewData,
    room: formattedRoomData,
  };
}

export async function insertNewReview(
  input: createReviewInput
): Promise<ICreatedReview> {
  logger.info(`Inserting new review for roomId: ${input.roomId}`);

  // Get the URI for the review service
  const reviewServiceURI: string | null = getInstanceURI(
    AppConstants.REVIEW_SERVICE
  );

  // Configure the Axios request
  const config: AxiosRequestConfig = {
    method: "POST",
    url: `${reviewServiceURI}${ApiConstants.REVIEW_SERVICE_API_V1}/${input.roomId}`,
    data: input,
  };

  try {
    const reviewData: ICreatedReview = (await axios.request(config)).data;
    logger.info(`Review inserted successfully: `, reviewData);

    return reviewData;
  } catch (error: any) {
    logger.error(`Error occurred while inserting new reiview: `, error);

    if (axios.isAxiosError(error)) {
      handleAxiosError(error);
    }

    throw error;
  }
}
