import { Request, Response } from "express";
import logger from "../config/logger";
import {
  createOrganization,
  findOrganization,
  getOrganization,
  getOrganizationBySuperAdmin,
  updateOrganization,
  verifyUser,
} from "../service/organization.service";
import { Error } from "mongoose";
import ApiConstants from "../constants/ApiConstants";
import { mapKeys, omit } from "lodash";
import {
  createOrganizationInput,
  getAllOrganizationsInput,
  updateOrganizationInput,
} from "../schema";
import { Organization } from "../model/types";
import { IGetOrganizations, IVerifyUser } from "../service";

export async function createOrganizationHandler(
  req: Request<{}, {}, createOrganizationInput>,
  res: Response
) {
  logger.info("Received organization creation request");

  const organizaionInput: createOrganizationInput = req.body;

  try {
    logger.info(`Creating organization for: `, organizaionInput);
    const createdOrganization: Organization = await createOrganization(
      organizaionInput
    );
    logger.info(`Organization created successfully: `, createdOrganization);

    const formattedOrganization = mapKeys(
      omit(JSON.parse(JSON.stringify(createdOrganization)), [
        "__v",
        "createdAt",
        "updatedAt",
        "admins",
      ]),
      (value, key) => {
        return key === "_id" ? "id" : key;
      }
    );

    res.status(201).send(formattedOrganization);
  } catch (error) {
    logger.error(`Error occured while creating organization`, error);
    return handleCreateOrganizationHandlerError(res, error);
  }
}

export async function updateOrganizationHandler(
  req: Request<{ id: string }, {}, updateOrganizationInput>,
  res: Response
) {
  logger.info("Received organization updation request");

  const organizaionInput: updateOrganizationInput = req.body;

  try {
    logger.info(`Updating organization for: `, organizaionInput);
    const updatedOrganization: Organization | null = await updateOrganization(
      organizaionInput
    );

    if (updatedOrganization === null) {
      logger.info(
        `couldn't find any organizaton with this id: ${organizaionInput.id}`
      );
      throw {
        name: "AppError",
        errorCode: "RECORD_NOT_FOUND",
        message: `couldn't find any organizaton with this id: ${organizaionInput.id}`,
        description: `couldn't find any organizaton with this id: ${organizaionInput.id}`,
        status: 404,
      };
    }

    logger.info(`Organization updated successfully: `, updatedOrganization);

    const formattedOrganization = {
      success: true,
      data: mapKeys(
        omit(JSON.parse(JSON.stringify(updatedOrganization)), [
          "__v",
          "createdAt",
          "updatedAt",
          "admins",
        ]),
        (value, key) => {
          return key === "_id" ? "id" : key;
        }
      ),
    };

    res.status(201).send(formattedOrganization);
  } catch (error) {
    logger.error(`Error occured while updating organization: `, error);
    return handleUpdateOrganizationHandlerError(res, error);
  }
}

export async function verifyUserHandler(
  req: Request<{ id: string; uid: string }, {}, {}>,
  res: Response
) {
  logger.info("Received verify user request");

  const organizaionId: string = req.params.id;
  const userId: string = req.params.uid;

  try {
    logger.info(
      `Verifying user: ${userId} for organizationId: ${organizaionId}`
    );
    const result: IVerifyUser = await verifyUser(organizaionId, userId);

    logger.info(`User Verified successfully: `, result);

    res.status(201).send(result);
  } catch (error) {
    logger.error(`Error occured while verifying user: `, error);
    return handleVerifyUserHandlerError(res, error);
  }
}

export async function getOrganizationHandler(
  req: Request<{ id: string }, {}, {}>,
  res: Response
) {
  logger.info("Received get organization data request");

  const organizaionId: string = req.params.id;

  try {
    logger.info(`Getting organization for organizaionId: ${organizaionId}`);

    const organization: Organization | null = await getOrganization(
      organizaionId
    );

    if (organization === null) {
      logger.info(
        `couldn't find any organizaton with this id: ${organizaionId}`
      );
      throw {
        name: "AppError",
        errorCode: "RECORD_NOT_FOUND",
        message: `couldn't find any organizaton with this id: ${organizaionId}`,
        description: `couldn't find any organizaton with this id: ${organizaionId}`,
        status: 404,
      };
    }

    logger.info(`Fetched organizaion: ${organization}`);

    const formattedOrganization = mapKeys(
      omit(JSON.parse(JSON.stringify(organization)), [
        "__v",
        "createdAt",
        "updatedAt",
        "admins",
      ]),
      (value, key) => {
        return key === "_id" ? "id" : key;
      }
    );

    res.status(201).send(formattedOrganization);
  } catch (error) {
    logger.error(`Error occured while fetching organization data: `, error);
    return handleGetOrganizationHandlerError(res, error);
  }
}

export async function getOrganizationByUserHandler(
  req: Request<{ uid: string }, {}, {}>,
  res: Response
) {
  logger.info("Received get organization data request");

  const uid: string = req.params.uid;

  try {
    logger.info(`Getting organization for userId: ${uid}`);

    const organization: Organization | null = await getOrganizationBySuperAdmin(
      uid
    );

    if (organization === null) {
      logger.info(`couldn't find any organizaton for userId: ${uid}`);
      throw {
        name: "AppError",
        errorCode: "RECORD_NOT_FOUND",
        message: `couldn't find any organizaton for userId: ${uid}`,
        description: `couldn't find any organizaton for userId: ${uid}`,
        status: 404,
      };
    }

    logger.info(`Fetched organizaion: ${organization}`);

    const formattedOrganization = mapKeys(
      omit(JSON.parse(JSON.stringify(organization)), ["__v"]),
      (value, key) => {
        return key === "_id" ? "id" : key;
      }
    );

    res.status(201).send(formattedOrganization);
  } catch (error) {
    logger.error(`Error occured while fetching organization data: `, error);
    return handleGetOrganizationHandlerError(res, error);
  }
}

export async function getOrganizationsHandler(
  req: Request<{}, {}, getAllOrganizationsInput>,
  res: Response
) {
  logger.info(`Received get all organizations request`);
  logger.info(`The query is: `, req.body);

  try {
    const { page = 1, limit = 10 } = req.body;
    const pageNumber = Number(page);
    const pageLimit = Number(limit);
    const query: any = createQuery(req.body);

    logger.info(`Getting all organizations the for query: `, query);
    const organizaions: IGetOrganizations = await findOrganization(
      pageNumber,
      pageLimit,
      query
    );
    logger.info(`Fetched all organizations successfully!`);

    res.send(organizaions);
  } catch (error: any) {
    logger.error(`Error occured while fetching organizations: `, error);
    return handleGetOrganizationsHandlerrError(res, error);
  }
}

function createQuery(input: any) {
  const mongoDBQuery: any = {};
  for (let key in input) {
    let field: string = key;
    let value: any = input[key];
    if (value !== null && ["page", "limit"].includes(field) === false) {
      mongoDBQuery[field] = value;
    }
  }
  return mongoDBQuery;
}

function handleCreateOrganizationHandlerError(res: Response, error: any) {
  let errorCode: string = ApiConstants.INTERNAL_SERVER_ERROR;
  let message: string = ApiConstants.MESSAGE_INTERNAL_SERVER_ERROR;
  let details: any = ApiConstants.DESCRIPTION_INTERNAL_SERVER_ERROR;
  let status: number = 500;

  if (error.code === 11000) {
    errorCode = ApiConstants.DUPLICATE_KEY;
    const duplicateKeys = Object.keys(error.keyPattern);
    if (duplicateKeys.length > 0) {
      message = `Organization with ${duplicateKeys.join(
        ", "
      )} already exists. Please choose different values.`;
    }
    details = duplicateKeys;
    status = 400;
  } else if (error instanceof Error.ValidationError) {
    errorCode = ApiConstants.BAD_REQUEST;
    message = "Couldn't create organization due to some invalid fields";
    details = Object.values(error.errors).map((err) => err.message)[0];
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

function handleUpdateOrganizationHandlerError(res: Response, error: any) {
  let errorCode: string = ApiConstants.INTERNAL_SERVER_ERROR;
  let message: string = ApiConstants.MESSAGE_INTERNAL_SERVER_ERROR;
  let details: any = ApiConstants.DESCRIPTION_INTERNAL_SERVER_ERROR;
  let status: number = 500;

  if (error.name === "CastError") {
    errorCode = ApiConstants.INVALID_ORGANIZATION_ID;
    message = ApiConstants.MESSAGE_INVALID_ROOM_ID;
    details = "Invalid organizationId";
    status = 400;
  } else if (error.code === 11000) {
    errorCode = ApiConstants.DUPLICATE_KEY;
    const duplicateKeys = Object.keys(error.keyPattern);
    if (duplicateKeys.length > 0) {
      message = `Organization with ${duplicateKeys.join(
        ", "
      )} already exists. Please choose different values.`;
    }
    details = duplicateKeys;
    status = 400;
  } else if (error instanceof Error.ValidationError) {
    errorCode = ApiConstants.BAD_REQUEST;
    message = "Couldn't create organization due to some invalid fields";
    details = Object.values(error.errors).map((err) => err.message)[0];
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

function handleGetOrganizationHandlerError(res: Response, error: any) {
  let errorCode: string = ApiConstants.INTERNAL_SERVER_ERROR;
  let message: string = ApiConstants.MESSAGE_INTERNAL_SERVER_ERROR;
  let details: any = ApiConstants.DESCRIPTION_INTERNAL_SERVER_ERROR;
  let status: number = 500;

  if (error.name === "CastError") {
    errorCode = ApiConstants.INVALID_ORGANIZATION_ID;
    message = ApiConstants.MESSAGE_INVALID_ROOM_ID;
    details = "Invalid organizationId";
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

function handleVerifyUserHandlerError(res: Response, error: any) {
  let errorCode: string = ApiConstants.INTERNAL_SERVER_ERROR;
  let message: string = ApiConstants.MESSAGE_INTERNAL_SERVER_ERROR;
  let details: any = ApiConstants.DESCRIPTION_INTERNAL_SERVER_ERROR;
  let status: number = 500;

  if (error.name === "CastError") {
    errorCode = ApiConstants.INVALID_ORGANIZATION_ID;
    message = ApiConstants.MESSAGE_INVALID_ROOM_ID;
    details = "Invalid organizationId";
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

function handleGetOrganizationsHandlerrError(res: Response, error: any) {
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
