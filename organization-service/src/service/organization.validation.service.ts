import logger from "../config/logger";
import ApiConstants from "../constants/ApiConstants";
import { Organization } from "../model/types";
import { updateOrganizationInput } from "../schema";
import { GenericValidInvalidEnum } from "../util";
import { getOrganization } from "./organization.service";

export async function validateUpdateOrganizationData(
  input: updateOrganizationInput
): Promise<GenericValidInvalidEnum> {
  logger.info("Validating createOrganizationInput input");

  // Get organization data
  const organizationData: Organization | null = await getOrganization(input.id);

  if (organizationData === null) {
    logger.info(`couldn't find any organizaton with this id: ${input.id}`);
    throw {
      name: "AppError",
      errorCode: "RECORD_NOT_FOUND",
      message: `couldn't find any organizaton with this id: ${input.id}`,
      description: `couldn't find any organizaton with this id: ${input.id}`,
      status: 404,
    };
  }

  // verify current user is allowed to update organization data or not
  const isAllowed: boolean = verifyUserPermission(input.uid, organizationData);

  return isAllowed === true
    ? GenericValidInvalidEnum.VALID
    : GenericValidInvalidEnum.INVALID;
}

export function verifyUserPermission(
  currentUserId: string,
  organizationData: Organization
): boolean {
  logger.info("Checking if the current user has the permissions.");

  logger.info(`Getting organization superAdmin`);
  const superAdmin = organizationData.superAdmin?.uid;
  logger.info(`Organization superAdmin id: ${superAdmin}`);

  // Checking if the current user is the Super Admin
  const isSuperAdmin: boolean = superAdmin === currentUserId;

  if (isSuperAdmin) {
    logger.info(`The current user have permission.`);
    return true;
  } else {
    logger.error(`The current user does not have permission.`);

    throw {
      name: "AppError",
      errorCode: ApiConstants.UNAUTHORIZED,
      message: "Only superAdmin have permission to update organization.",
      details:
        "The current user is not recognized as an organization superAdmin",
      status: 401,
    };
  }
}
