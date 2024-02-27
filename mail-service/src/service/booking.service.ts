import axios, { AxiosRequestConfig } from "axios";
import logger from "../config/logger";
import AppConstants from "../constants/AppConstants";
import { getInstanceURI, handleAxiosError } from "../utils";
import ApiConstants from "../constants/ApiConstants";
import { IBookingData } from "./types";

export async function getBookingDetails(id: string): Promise<IBookingData> {
  logger.info(`Attempting to get booking details for id:`, id);

  // Get the URI for the booking service
  const bookingServiceURI: string | null = getInstanceURI(
    AppConstants.BOOKING_SERVICE
  );

  // Configure the Axios request
  const config: AxiosRequestConfig = {
    method: "GET",
    url: `${bookingServiceURI}${ApiConstants.BOOKING_SERVICE_API_V1}/${id}`,
  };

  try {
    const bookingData: IBookingData = (await axios.request(config)).data;
    logger.info(`Booking data retrived successfully: `, bookingData);

    return bookingData;
  } catch (error: any) {
    logger.error(`Error occurred while retrieving booking data: `, error);

    if (axios.isAxiosError(error)) {
      handleAxiosError(error);
    }

    throw error;
  }
}
