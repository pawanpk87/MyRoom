import { mapKeys, omit } from "lodash";
import logger from "../config/logger";
import { IGetAllReview } from "../controller";
import { Review } from "../model";
import ReviewModel from "../model/review.model";
import { addReviewInput } from "../schema";
import { ICreatedReview } from "./types";

export async function createReview(
  roomId: string,
  review: addReviewInput
): Promise<ICreatedReview> {
  logger.info("Attempting review creation with data: ", review);

  const newReview: Review = await ReviewModel.create(review);

  const result = await ReviewModel.aggregate([
    {
      $match: {
        roomId: roomId,
      },
    },
    {
      $group: {
        _id: null,
        sumOfRatings: { $sum: "$rating" },
        totalReviewCount: { $sum: 1 },
      },
    },
  ]);

  const formattedRoom = mapKeys(
    omit(JSON.parse(JSON.stringify(newReview)), [
      "__v",
      "createdAt",
      "updatedAt",
    ]),
    (value, key) => {
      return key === "_id" ? "id" : key;
    }
  );

  const createReview: ICreatedReview = {
    review: formattedRoom,
    sumOfRatings: result[0].sumOfRatings,
    totalReviewCount: result[0].totalReviewCount,
  };

  return createReview;
}

export async function getReview(id: string): Promise<Review | null> {
  return await ReviewModel.findById(id);
}

export async function getOverallRating(roomId: string): Promise<any | null> {
  const ratings = await ReviewModel.aggregate([
    { $match: { roomId: roomId } },
    { $group: { _id: "$rating", count: { $sum: 1 } } },
  ]);

  let overallRating = 0;
  let totalReviews = 0;

  const statistics: { [key: string]: number } = {};

  ratings.forEach((rating: { _id: number; count: number }) => {
    const star = `${rating._id}star`;
    statistics[star] = rating.count;
    overallRating += rating._id * rating.count;
    totalReviews += rating.count;
  });

  overallRating = overallRating / totalReviews;

  return {
    statistics,
    overall: Number(overallRating.toFixed(1)),
  };
}

export async function getAllReview(
  page: number,
  limit: number,
  query: any
): Promise<IGetAllReview> {
  const { sort = {}, ...findQuery } = query;

  const reviews: any = await ReviewModel.find(findQuery)
    .sort(sort)
    .limit(limit * 1)
    .skip((page - 1) * limit)
    .exec();

  const count = await ReviewModel.countDocuments(query);

  return {
    reviews,
    totalRecods: count,
    totalPages: Math.ceil(count / limit),
    currentPage: page,
  } as IGetAllReview;
}
