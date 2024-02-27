import { Request, Response } from "express";
import logger from "../config/logger";
import { addReviewInput, getAllReviewQuery } from "../schema";
import ApiConstants from "../constants/ApiConstants";
import { mapKeys, omit } from "lodash";
import {
  ICreatedReview,
  createReview,
  getAllReview,
  getOverallRating,
  getReview,
} from "../service";
import { IGetAllReview } from "./types";
import { Review } from "../model";

export async function addReviewHandler(
  req: Request<{ roomId: string }, {}, addReviewInput>,
  res: Response
) {
  logger.info("Received request for add new review");

  const roomId: string = req.params.roomId;
  const review: addReviewInput = req.body;

  try {
    logger.info("Adding review for: ", review);
    const addedReview: ICreatedReview = await createReview(roomId, review);
    logger.info("Review added successfully!");

    res.status(201).send(addedReview);
  } catch (error) {
    logger.error("Error occured while adding new review");
    return handleCreateReviewHandlerError(res, error);
  }
}

export async function getReviewHandler(
  req: Request<{ id: string }, {}, {}>,
  res: Response
) {
  logger.info(`Received get review data request`);

  const reviewId: string = req.params.id;

  try {
    logger.info(`Fetching review data for reviewId: ${reviewId}`);
    const review: Review | null = await getReview(reviewId);

    if (review === null) {
      logger.info(`couldn't find any review with this id: ${reviewId}`);
      throw {
        name: "AppError",
        errorCode: "RECORD_NOT_FOUND",
        message: `couldn't find any review with this id: ${reviewId}`,
        details: `couldn't find any review with this id: ${reviewId}`,
        status: 404,
      };
    }

    logger.info(`Fetched review: ${review}`);

    const formattedReview = mapKeys(
      omit(JSON.parse(JSON.stringify(review)), ["__v"]),
      (value, key) => {
        return key === "_id" ? "id" : key;
      }
    );

    res.status(200).send(formattedReview);
  } catch (error) {
    logger.error(`Error occured while fetching room data: `, error);
    return handleGetReviewHandlerError(res, error);
  }
}

export async function getOverallRatingHandler(
  req: Request<{ roomId: string }, {}, {}>,
  res: Response
) {
  logger.info(`Received get overall review data request`);

  const roomId: string = req.params.roomId;

  try {
    logger.info(`Fetching overall rating for roomId: ${roomId}`);
    const overallRating: any = await getOverallRating(roomId);
    logger.info(`Fetched overall rating: ${overallRating}`);

    res.status(200).send(overallRating);
  } catch (error) {
    logger.error(`Error occured while fetching room data: `, error);
    return handleGetReviewHandlerError(res, error);
  }
}

export async function getAllReviewByRoomHandler(
  req: Request<{}, {}, {}, getAllReviewQuery>,
  res: Response
) {
  logger.info(`Received request for getting all reviews`);
  logger.info(`The query is: `, req.query);

  try {
    const { page = 1, limit = 10 } = req.query;
    const pageNumber = Number(page);
    const pageLimit = Number(limit);
    const query: any = createQuery(req.query);

    logger.info(`Getting all rooms the for query: `, query);
    const reviews: IGetAllReview = await getAllReview(
      pageNumber,
      pageLimit,
      query
    );
    logger.info(`Fetched all reviews successfully!`);

    reviews.reviews = reviews.reviews.map((room: any) => {
      return mapKeys(
        omit(JSON.parse(JSON.stringify(room)), ["__v"]),
        (value, key) => {
          return key === "_id" ? "id" : key;
        }
      );
    });

    res.status(200).send(reviews);
  } catch (error) {
    logger.error(`Error occured while fetching reviews: `, error);
    return handleGetAllReviewHandlerError(res, error);
  }
}

function createQuery(input: any) {
  const queryProperties = [
    "roomId",
    "organizationId",
    "rating",
    "uid",
    "sorting",
    "page",
    "limit",
  ];
  function convertObjectToKeyValueArray(obj: any, parentKey: string): any[] {
    const result: any[] = [];
    if (["rating"].includes(parentKey)) {
      for (const key in obj) {
        const value = obj[key];
        result.push({
          key: `${parentKey}.${key}`,
          value: value[key],
          operator: value["operator"],
        });
      }
    } else {
      result.push({
        key: parentKey,
        value: obj[parentKey],
        operator: obj["operator"],
      });
    }
    return result;
  }

  let mongoDBQuery: any = {};

  for (let key in input) {
    let field: string = key;
    let value: any = input[key];

    if (queryProperties.includes(field)) {
      if (
        typeof value === "object" &&
        !Array.isArray(value) &&
        value !== null
      ) {
        const nestedFields: any[] = convertObjectToKeyValueArray(value, field);
        nestedFields.forEach((data) => {
          const operator = data.operator;
          let nestedField = data.key;
          let nestedFieldValue = data.value;
          switch (operator) {
            case "EQUAL":
              mongoDBQuery[nestedField] = {
                $eq: nestedFieldValue,
              };
              break;

            case "NOT_EQUAL":
              mongoDBQuery[nestedField] = {
                $ne: nestedFieldValue,
              };
              break;

            case "GREATER_THAN":
              mongoDBQuery[nestedField] = {
                $gt: nestedFieldValue,
              };
              break;

            case "LESS_THAN":
              mongoDBQuery[nestedField] = {
                $lt: nestedFieldValue,
              };
              break;

            case "GREATER_THAN_OR_EQUAL":
              mongoDBQuery[nestedField] = {
                $gte: nestedFieldValue,
              };
              break;

            case "LESS_THAN_OR_EQUAL":
              mongoDBQuery[nestedField] = {
                $lte: nestedFieldValue,
              };

            default:
          }
        });
      } else if (value !== null && Array.isArray(value)) {
        if (field === "sorting") {
          const allFields: any = {};
          value.forEach((data: any) => {
            allFields[data.field] = Number(data.operator);
          });
          mongoDBQuery = {
            ...mongoDBQuery,
            sort: allFields,
          };
        } else {
          mongoDBQuery[field] = {
            $in: value,
          };
        }
      } else if (
        value !== null &&
        ["page", "limit"].includes(field) === false
      ) {
        mongoDBQuery[field] = value;
      }
    }
  }
  return mongoDBQuery;
}

function handleCreateReviewHandlerError(res: Response, error: any) {
  let errorCode: string = ApiConstants.INTERNAL_SERVER_ERROR;
  let message: string = ApiConstants.MESSAGE_INTERNAL_SERVER_ERROR;
  let details: any = ApiConstants.DESCRIPTION_INTERNAL_SERVER_ERROR;
  let status: number = 500;

  if (error.name === "AppError") {
    errorCode = error.errorCode;
    message = error.message;
    details = error.details;
    status = error.status;
  }

  return res.status(status).send({
    errorCode,
    message,
    details,
  });
}

function handleGetReviewHandlerError(res: Response, error: any) {
  let errorCode: string = ApiConstants.INTERNAL_SERVER_ERROR;
  let message: string = ApiConstants.MESSAGE_INTERNAL_SERVER_ERROR;
  let details: any = ApiConstants.DESCRIPTION_INTERNAL_SERVER_ERROR;
  let status: number = 500;

  if (error.name === "CastError") {
    errorCode = ApiConstants.INVALID_ROOM_ID;
    message = ApiConstants.MESSAGE_INVALID_ROOM_ID;
    details = "Invalid reviewId";
    status = 400;
  } else if (error.name === "AppError") {
    errorCode = error.errorCode;
    message = error.message;
    details = error.details;
    status = error.status;
  }

  return res.status(status).send({
    errorCode,
    message,
    details,
  });
}

function handleGetAllReviewHandlerError(res: Response, error: any) {
  let errorCode: string = ApiConstants.INTERNAL_SERVER_ERROR;
  let message: string = ApiConstants.MESSAGE_INTERNAL_SERVER_ERROR;
  let details: any = ApiConstants.DESCRIPTION_INTERNAL_SERVER_ERROR;
  let status: number = 500;

  if (error.name === "AppError") {
    errorCode = error.errorCode;
    message = error.message;
    details = error.details;
    status = error.status;
  }

  return res.status(status).send({
    errorCode,
    message,
    details,
  });
}
