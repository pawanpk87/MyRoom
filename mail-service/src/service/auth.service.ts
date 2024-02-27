import axios, { AxiosRequestConfig } from "axios";
import logger from "../config/logger";
import { getInstanceURI, handleAxiosError } from "../utils";
import { IUser } from "./types";
import ApiConstants from "../constants/ApiConstants";
import AppConstants from "../constants/AppConstants";

export async function getUserDetails(id: string): Promise<IUser> {
  logger.info(`Attempting to get user details for id:`, id);

  // Get the URI for the auth server
  const authServiceURI: string | null = getInstanceURI(
    AppConstants.AUTH_SERVICE
  );

  // Configure the Axios request
  const config: AxiosRequestConfig = {
    method: "GET",
    url: `${authServiceURI}${ApiConstants.AUTH_SERVER}/users/${id}`,
  };

  try {
    const userData: IUser = (await axios.request(config)).data;
    logger.info(`User data retrived successfully: `, userData);

    return userData;
  } catch (error: any) {
    logger.error(`Error occurred while retrieving the User data: `, error);

    if (axios.isAxiosError(error)) {
      handleAxiosError(error);
    }

    throw error;
  }
}
