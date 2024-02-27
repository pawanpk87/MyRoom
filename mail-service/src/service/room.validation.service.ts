import logger from "../config/logger";
import ApiConstants from "../constants/ApiConstants";
import { createRoomInput, deleteRoomInput, updateRoomInput } from "../schema";
import { GenericValidInvalidEnum } from "../utils/types";
import {
  getOrganizationDataById,
  getOrganizationDataByRoomId,
} from "./organization.service";
import { IOrganizationData } from "./types";

export async function validateCreateRoomData(
  input: createRoomInput
): Promise<GenericValidInvalidEnum> {
  logger.info("Validating createRoomInput input");

  // Get organization data
  const organizationData: IOrganizationData = await getOrganizationDataById(
    input.organizationId
  );

  // verify current user is allowed to create room or not
  const isAllowed: boolean = verifyUserPermission(input.uid, organizationData);

  // verify available start should be greater that than current date
  // room capacity should not be more than 100
  // verify prices should not be 0
  // currency should bot be other than INR

  return isAllowed === true
    ? GenericValidInvalidEnum.VALID
    : GenericValidInvalidEnum.INVALID;
}

export async function validateUpdateRoomData(
  input: updateRoomInput
): Promise<GenericValidInvalidEnum> {
  logger.info("Validating updateRoomInput input");

  // Get organization data
  const organizationData: IOrganizationData = await getOrganizationDataByRoomId(
    input.id
  );

  // verify current user is allowed to update room or not
  const isAllowed: boolean = verifyUserPermission(input.uid, organizationData);

  return isAllowed === true
    ? GenericValidInvalidEnum.VALID
    : GenericValidInvalidEnum.INVALID;
}

export async function validateDeleteRoomData(
  input: deleteRoomInput
): Promise<GenericValidInvalidEnum> {
  // Get organization data
  const organizationData: IOrganizationData = await getOrganizationDataByRoomId(
    input.id
  );

  // verify current user is allowed to delete room or not
  const isAllowed: boolean = verifyUserPermission(input.uid, organizationData);

  return isAllowed === true
    ? GenericValidInvalidEnum.VALID
    : GenericValidInvalidEnum.INVALID;
}

function verifyUserPermission(
  currentUserId: string,
  organizationData: IOrganizationData
): boolean {
  logger.info("Checking if the current user has the permissions.");

  logger.info(`Getting all organization admins`);
  const superAdmin = organizationData.superAdmin?.uid;
  const admins = (organizationData.admins ?? []).map((data: any) => {
    return data.uid;
  });
  const allAdmins = [superAdmin, ...admins];
  logger.info(`All organization admins: ${allAdmins}`);

  // Checking if the current user is an organization admin
  const isAdminIdExists: boolean = allAdmins.includes(currentUserId);

  if (isAdminIdExists) {
    logger.info(`The current user have permission.`);
    return true;
  } else {
    logger.error(`The current user does not have permission.`);

    throw {
      name: "AppError",
      errorCode: ApiConstants.UNAUTHORIZED,
      message: "Only admin have permission to create or update rooms.",
      details: "The current user is not recognized as an organization admin",
      status: 401,
    };
  }
}
