import axios, { AxiosRequestConfig } from "axios";
import logger from "../config/logger";
import ApiConstants from "../constants/ApiConstants";
import AppConstants from "../constants/AppConstants";
import { getInstanceURI } from "../utils/eurekaClientUtil";
import { IOrganizationData } from "./types";
import RoomModel from "../model/room.model";
import { handleAxiosError } from "../utils/utils";

export async function getOrganizationDataById(
  id: string
): Promise<IOrganizationData> {
  logger.info(`Fetching organization data for organizationId: ${id}`);

  // Get the URI for the organization service
  const organizationServiceURI: string | null = getInstanceURI(
    AppConstants.ORGANIZATION_SERVICE
  );

  logger.info(
    `Calling Service ${AppConstants.ORGANIZATION_SERVICE} for fetching organization data`
  );

  // Configure the Axios request
  const config: AxiosRequestConfig = {
    method: "GET",
    url: `${organizationServiceURI}${ApiConstants.ORGANIZATION_SERVICE_API_V1}/org/${id}`,
  };

  try {
    const organizatinData: IOrganizationData = (await axios.request(config))
      .data;
    logger.info(`Organization data Fetched successfully: `, organizatinData);

    return organizatinData;
  } catch (error: any) {
    logger.error(`Error fetching organization data: `, error);

    if (axios.isAxiosError(error)) {
      handleAxiosError(error);
    }

    throw error;
  }
}

export async function getOrganizationDataByRoomId(
  id: string | undefined
): Promise<IOrganizationData> {
  logger.info(`Attempting to get organization data using room id:`, id);

  try {
    logger.info("Fetching organizationId of room");
    const room = await RoomModel.findById(id).select("organizationId");

    if (!room) {
      const message = `couldn't find any room with this id: ${id}`;
      logger.error(message);
      throw {
        name: "RECORD_NOT_FOUND",
        message: message,
        description: `Invalid room id:${id}`,
      };
    }

    const organizationId = room.organizationId;
    logger.info(`Fetched organizationId: ${organizationId}`);

    return await getOrganizationDataById(organizationId);
  } catch (error: any) {
    logger.error(`Error while fetching organization data by room id: ${id}`);
    throw error;
  }
}
