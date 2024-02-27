import { AxiosError } from "axios";
import logger from "../config/logger";
import { IRoomServiceError } from "./types";

export function handleAxiosError(error: AxiosError): IRoomServiceError {
  logger.error(`Handling AxiosError: `, error);

  if (error.response) {
    logger.error(`Request failed with status ${error.response.status}`);
    if (error.response.data) {
      const { errorCode, message, details }: any = error.response.data;
      throw {
        name: "AppError",
        errorCode: errorCode,
        message: message,
        details: details,
        status: 500,
      };
    }
  } else if (error.request) {
    logger.error(`Request failed to reach to server`);
  } else {
    logger.error(`Unexpected Axios error occured`);
  }

  throw error;
}

export function calculateOverallRating(
  sumOfRatings: number,
  totalReviewCount: number
): number {
  const averageRating = sumOfRatings / totalReviewCount;

  const overallRating = (averageRating / 5) * 5;

  return Math.round(overallRating * 10) / 10;
}

export function decodeQueryParams(query: any): any {
  const decodedQuery: any = {};

  for (const [key, value] of Object.entries(query)) {
    try {
      decodedQuery[key] = JSON.parse(decodeURIComponent(value as string));
    } catch (error) {
      decodedQuery[key] = value;
    }
  }

  return decodedQuery;
}
