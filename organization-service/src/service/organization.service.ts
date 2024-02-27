import logger from "../config/logger";
import { createOrganizationInput, updateOrganizationInput } from "../schema";
import OrganizationModel from "../model/organization.model";
import {
  Addresses,
  Admin,
  AdminType,
  BusinessProfile,
  Organization,
} from "../model/types";
import { GenericValidInvalidEnum } from "../util";
import {
  validateUpdateOrganizationData,
  verifyUserPermission,
} from "./organization.validation.service";
import ApiConstants from "../constants/ApiConstants";
import { IGetOrganizations, IVerifyUser } from "./types";

export async function createOrganization(
  input: createOrganizationInput
): Promise<Organization> {
  logger.info(`Attempting organization creation with input: `, input);

  const { name, email, description, phone, uid, businessProfile } = input;

  // Create super admin
  const superAdmin: Admin = {
    uid: uid,
    adminType: AdminType.SUPER_ADMIN,
  };

  // Create address
  const addresses: Addresses = {
    street1: businessProfile.addresses.street1,
    street2: businessProfile.addresses.street2,
    city: businessProfile.addresses.city,
    state: businessProfile.addresses.state,
    postalCode: businessProfile.addresses.postalCode,
    country: businessProfile.addresses.country,
  };

  // Create business profile data
  const businessProfileData: BusinessProfile = {
    businessType: businessProfile.businessType,
    category: businessProfile.category,
    subcategory: businessProfile.subcategory,
    addresses: addresses,
  };

  const organizaion: Organization = {
    name: name,
    email: email,
    description: description,
    phone: phone,
    superAdmin: superAdmin,
    businessProfile: businessProfileData,
  };

  return await OrganizationModel.create(organizaion);
}

export async function updateOrganization(
  input: updateOrganizationInput
): Promise<Organization | null> {
  logger.info(`Attempting organization updation with input: `, input);

  // validate updateOrganization data
  const valid: GenericValidInvalidEnum = await validateUpdateOrganizationData(
    input
  );

  if (valid === GenericValidInvalidEnum.VALID) {
    const { uid, id, ...userGivenData } = input;

    return await OrganizationModel.findByIdAndUpdate(
      input.id,
      {
        $set: userGivenData,
      },
      { new: true }
    );
  } else {
    throw {
      name: "AppError",
      errorCode: ApiConstants.INVALID_DATA,
      message: "Invalid room data",
      details: "",
      status: 400,
    };
  }
}

export async function verifyUser(
  organizaionId: string,
  userId: string
): Promise<IVerifyUser> {
  logger.info(`Attempting to verify user`);

  // Get organization data
  const organizationData: Organization | null = await getOrganization(
    organizaionId
  );

  if (organizationData === null) {
    logger.info(`couldn't find any organizaton with this id: ${organizaionId}`);
    throw {
      name: "AppError",
      errorCode: "RECORD_NOT_FOUND",
      message: `couldn't find any organizaton with this id: ${organizaionId}`,
      description: `couldn't find any organizaton with this id: ${organizaionId}`,
      status: 404,
    };
  }

  // verify current user
  const isAllowed: boolean = verifyUserPermission(userId, organizationData);

  return {
    allowed: isAllowed,
    message: isAllowed
      ? "User has the required permissions."
      : "User does not have the required permissions.",
  } as IVerifyUser;
}

export async function getOrganization(
  id: string
): Promise<Organization | null> {
  return await OrganizationModel.findById(id);
}

export async function getOrganizationBySuperAdmin(
  uid: string
): Promise<Organization | null> {
  console.log("getOrganizationBySuperAdmin uid is");
  console.log(uid);
  return await OrganizationModel.findOne({
    "superAdmin.uid": uid,
  });
}

export async function findOrganization(
  page: number,
  limit: number,
  query: any
): Promise<IGetOrganizations> {
  const organizaions: any = await OrganizationModel.find(query)
    .limit(limit * 1)
    .skip((page - 1) * limit)
    .exec();
  const count = await OrganizationModel.countDocuments(query);
  return {
    organizaions,
    totalRecods: count,
    totalPages: Math.ceil(count / limit),
    currentPage: page,
  } as IGetOrganizations;
}
