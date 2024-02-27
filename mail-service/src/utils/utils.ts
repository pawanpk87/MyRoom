import { AxiosError } from "axios";
import logger from "../config/logger";
import { IMailServiceError } from "./types";

export function handleAxiosError(error: AxiosError): IMailServiceError {
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

export function formatShortDate(date: Date): string {
  date = new Date(date);
  return date
    .toLocaleDateString("en-GB", {
      day: "numeric",
      month: "short",
      year: "numeric",
    })
    .replace(/ /g, "-");
}
