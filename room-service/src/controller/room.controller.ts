import { Request, Response } from "express";
import logger from "../config/logger";
import {
  checkRoomAvailability,
  createRoom,
  deleteRoom,
  deleteRooms,
  getRoom,
  getRooms,
  updateRoom,
  updateRoomForBooking,
} from "../service/room.service";
import { Room } from "../model";
import {
  availabilityInput,
  createRoomInput,
  deleteAllRoomInput,
  deleteRoomInput,
  getAllRoomQueryInput,
  updateRoomInput,
  updateRoomSchemaForBookingTypeInput,
} from "../schema";
import { Error } from "mongoose";
import { IGetRooms, IRoomAvailability } from "../service/types";
import { mapKeys, omit } from "lodash";
import ApiConstants from "../constants/ApiConstants";
import { decodeQueryParams } from "../utils";

export async function createRoomHandler(
  req: Request<{}, {}, createRoomInput>,
  res: Response
) {
  logger.info("Received room creation request");

  const room: createRoomInput = req.body;

  try {
    logger.info(`Creating room for:`, room);
    const createdRoom: Room = await createRoom(room);
    logger.info(`Room created successfully: `, createdRoom);

    const formattedRoom = mapKeys(
      omit(JSON.parse(JSON.stringify(createdRoom)), [
        "__v",
        "createdAt",
        "updatedAt",
      ]),
      (value, key) => {
        return key === "_id" ? "id" : key;
      }
    );

    res.status(201).send(formattedRoom);
  } catch (error) {
    logger.error(`Error occurred while creating room`, error);
    return handleCreateRoomHandlerError(res, error);
  }
}

export async function updateRoomHandler(
  req: Request<{ id: string }, {}, updateRoomInput>,
  res: Response
) {
  logger.info("Received room updation request");

  const id: string = req.params.id;
  const room: updateRoomInput = req.body;
  room.id = id;

  try {
    logger.info(`Updating room: `, room);
    const updatedRoom = await updateRoom(room);

    if (updatedRoom === null) {
      logger.info(`couldn't find any room with this id: ${id}`);
      throw {
        name: "AppError",
        errorCode: "RECORD_NOT_FOUND",
        message: `couldn't find any room with this id: ${id}`,
        description: `couldn't find any room with this id: ${id}`,
        status: 404,
      };
    }

    logger.info(`Updated room successfully: `, updatedRoom);

    const formattedRoom = {
      success: true,
      data: mapKeys(
        omit(JSON.parse(JSON.stringify(updatedRoom)), [
          "__v",
          "createdAt",
          "updatedAt",
        ]),
        (value, key) => {
          return key === "_id" ? "id" : key;
        }
      ),
    };

    res.status(200).send(formattedRoom);
  } catch (error) {
    logger.error(`Error occured while updating room: `, error);
    return handleUpdateRoomHandlerError(res, error);
  }
}

export async function updateRoomForBookingHandler(
  req: Request<{ id: string }, {}, updateRoomSchemaForBookingTypeInput>,
  res: Response
) {
  logger.info("Received room updation request");

  const id: string = req.params.id;
  const room: updateRoomSchemaForBookingTypeInput = req.body;
  room.id = id;

  try {
    logger.info(`Updating room: `, room);
    const updatedRoom = await updateRoomForBooking(room);

    if (updatedRoom === null) {
      logger.info(`couldn't find any room with this id: ${id}`);
      throw {
        name: "AppError",
        errorCode: "RECORD_NOT_FOUND",
        message: `couldn't find any room with this id: ${id}`,
        description: `couldn't find any room with this id: ${id}`,
        status: 404,
      };
    }

    logger.info(`Updated room successfully: `, updatedRoom);

    const formattedRoom = {
      success: true,
      data: mapKeys(
        omit(JSON.parse(JSON.stringify(updatedRoom)), [
          "__v",
          "createdAt",
          "updatedAt",
        ]),
        (value, key) => {
          return key === "_id" ? "id" : key;
        }
      ),
    };

    res.status(200).send(formattedRoom);
  } catch (error) {
    logger.error(`Error occured while updating room: `, error);
    return handleUpdateRoomHandlerError(res, error);
  }
}

export async function getRoomHandler(
  req: Request<{ id: string }, {}, {}>,
  res: Response
) {
  logger.info(`Received get room data request`);

  const roomId: string = req.params.id;

  try {
    logger.info(`Fetching room data for roomId: ${roomId}`);
    const room: Room | null = await getRoom(roomId);

    if (room === null) {
      logger.info(`couldn't find any room with this id: ${roomId}`);
      throw {
        name: "AppError",
        errorCode: "RECORD_NOT_FOUND",
        message: `couldn't find any room with this id: ${roomId}`,
        details: `couldn't find any room with this id: ${roomId}`,
        status: 404,
      };
    }

    logger.info(`Fetched room: ${room}`);

    const formattedRoom = mapKeys(
      omit(JSON.parse(JSON.stringify(room)), ["__v"]),
      (value, key) => {
        return key === "_id" ? "id" : key;
      }
    );

    res.status(200).send(formattedRoom);
  } catch (error) {
    logger.error(`Error occured while fetching room data: `, error);
    return handleGetRoomHandlerError(res, error);
  }
}

export async function deleteRoomHandler(
  req: Request<{ id: string }, {}, deleteRoomInput>,
  res: Response
) {
  logger.info(`Received room deletion request`);

  const input: deleteRoomInput = req.body;

  try {
    logger.info(`Deletting room: ${input.id}`);
    const deletedRoom = await deleteRoom(input);
    logger.info(`Deleted room successfully: `, deleteRoom);

    const formattedRoom = {
      success: true,
      data: mapKeys(
        omit(JSON.parse(JSON.stringify(deletedRoom)), [
          "__v",
          "createdAt",
          "updatedAt",
        ]),
        (value, key) => {
          return key === "_id" ? "id" : key;
        }
      ),
    };

    res.status(200).send(formattedRoom);
  } catch (error) {
    logger.error(`Error occured while deleting the room: `, error);
    return handleDeleteRoomHandlerError(res, error);
  }
}

export async function checkAvailabilityHandler(
  req: Request<{ id: string }, {}, availabilityInput>,
  res: Response
) {
  logger.info(`Received request for checking room availability`);

  req.body.id = req.params.id;

  try {
    logger.info(
      `Checking room availability for roomId: ${req.params.id} and with data:`,
      req.body
    );
    const result: IRoomAvailability = await checkRoomAvailability(
      req.params.id,
      req.body
    );
    logger.info("Room availability: ", result);

    res.status(200).send(result);
  } catch (error) {
    logger.error(`Error occured while checking the availability: `, error);
    return handleCheckAvailabilityHandlerError(res, error);
  }
}

export async function getRoomsHandler(req: Request<{}, {}, {}>, res: Response) {
  logger.info(`Received get all room request`);
  logger.info(`The query is: `, req.query);

  try {
    const { page = 1, limit = 10 } = req.query;
    const pageNumber = Number(page);
    const pageLimit = Number(limit);
    const query: any = createQuery(decodeQueryParams(req.query));

    console.log("generated query is");
    console.log(query);

    logger.info(`Getting all rooms the for query: `, query);
    const rooms: IGetRooms = await getRooms(pageNumber, pageLimit, query);
    logger.info(`Fetched all rooms successfully!`);

    rooms.rooms = rooms.rooms.map((room: any) => {
      return mapKeys(
        omit(JSON.parse(JSON.stringify(room)), ["__v"]),
        (value, key) => {
          return key === "_id" ? "id" : key;
        }
      );
    });

    res.status(200).send(rooms);
  } catch (error) {
    logger.error(`Error occured while fetching rooms: `, error);
    return handleGetRoomsHandlerError(res, error);
  }
}

export async function deleteRoomsHandler(
  req: Request<{}, {}, deleteAllRoomInput>,
  res: Response
) {
  logger.info(`Received delete rooms request`);
  try {
    logger.info(`Deletting room: ${req.body.ids}`);
    const result = await deleteRooms(req.body);
    logger.info(`Rooms deleted successfully`);

    res.status(200).send({
      success: true,
      count: result,
    });
  } catch (error) {
    logger.error(`Error occured while deleting rooms: `, error);
    return handleDeleteRoomHandlerError(res, error);
  }
}

function createQuery(input: any) {
  console.log(input);

  const queryProperties = [
    "organizationId",
    "title",
    "availableDates",
    "cost",
    "rating",
    "capacity",
    "bookingStatus",
    "amenities",
    "features",
    "city",
    "checkInType",
    "sorting",
    "page",
    "limit",
  ];

  function convertObjectToKeyValueArray(obj: any, parentKey: string): any[] {
    const result: any[] = [];
    if (["availableDates", "rating"].includes(parentKey)) {
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
            $all: value,
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

function handleCreateRoomHandlerError(res: Response, error: any) {
  let errorCode: string = ApiConstants.INTERNAL_SERVER_ERROR;
  let message: string = ApiConstants.MESSAGE_INTERNAL_SERVER_ERROR;
  let details: any = ApiConstants.DESCRIPTION_INTERNAL_SERVER_ERROR;
  let status: number = 500;

  if (error.code === 11000) {
    errorCode = ApiConstants.DUPLICATE_KEY;
    const duplicateKeys = Object.keys(error.keyPattern);
    if (duplicateKeys.length > 0) {
      message = `Room with ${duplicateKeys.join(
        ", "
      )} already exists. Please choose different values.`;
    }
    details = duplicateKeys;
    status = 400;
  } else if (error instanceof Error.ValidationError) {
    errorCode = ApiConstants.BAD_REQUEST;
    message = "Couldn't create room due to some invalid fields";
    details = Object.values(error.errors).map((err) => err.message);
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

function handleUpdateRoomHandlerError(res: Response, error: any) {
  let errorCode: string = ApiConstants.INTERNAL_SERVER_ERROR;
  let message: string = ApiConstants.MESSAGE_INTERNAL_SERVER_ERROR;
  let details: any = ApiConstants.DESCRIPTION_INTERNAL_SERVER_ERROR;
  let status: number = 500;

  if (error.name === "CastError") {
    errorCode = ApiConstants.INVALID_ROOM_ID;
    message = ApiConstants.MESSAGE_INVALID_ROOM_ID;
    details = "Invalid roomId";
    status = 400;
  } else if (error.code === 11000) {
    errorCode = ApiConstants.DUPLICATE_KEY;
    const duplicateKeys = Object.keys(error.keyPattern);
    if (duplicateKeys.length > 0) {
      message = `Room with ${duplicateKeys.join(
        ", "
      )} already exists. Please choose different values.`;
    }
    details = duplicateKeys;
    status = 400;
  } else if (error instanceof Error.ValidationError) {
    errorCode = ApiConstants.BAD_REQUEST;
    message = "Couldn't create room due to some invalid fields";
    details = Object.values(error.errors).map((err) => err.message);
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

function handleDeleteRoomHandlerError(res: Response, error: any) {
  let errorCode: string = ApiConstants.INTERNAL_SERVER_ERROR;
  let message: string = ApiConstants.MESSAGE_INTERNAL_SERVER_ERROR;
  let details: any = ApiConstants.DESCRIPTION_INTERNAL_SERVER_ERROR;
  let status: number = 500;

  if (error.name === "CastError") {
    errorCode = ApiConstants.INVALID_ROOM_ID;
    message = ApiConstants.MESSAGE_INVALID_ROOM_ID;
    details = "Invalid roomId";
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

function handleGetRoomHandlerError(res: Response, error: any) {
  let errorCode: string = ApiConstants.INTERNAL_SERVER_ERROR;
  let message: string = ApiConstants.MESSAGE_INTERNAL_SERVER_ERROR;
  let details: any = ApiConstants.DESCRIPTION_INTERNAL_SERVER_ERROR;
  let status: number = 500;

  if (error.name === "CastError") {
    errorCode = ApiConstants.INVALID_ROOM_ID;
    message = ApiConstants.MESSAGE_INVALID_ROOM_ID;
    details = "Invalid roomId";
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

function handleCheckAvailabilityHandlerError(res: Response, error: any) {
  let errorCode: string = ApiConstants.INTERNAL_SERVER_ERROR;
  let message: string = ApiConstants.MESSAGE_INTERNAL_SERVER_ERROR;
  let details: any = ApiConstants.DESCRIPTION_INTERNAL_SERVER_ERROR;
  let status: number = 500;

  if (error.name === "CastError") {
    errorCode = ApiConstants.INVALID_ROOM_ID;
    message = ApiConstants.MESSAGE_INVALID_ROOM_ID;
    details = "Invalid roomId";
    status = 400;
  } else if (error.name === "AppError") {
    errorCode = error.errorCode;
    message = error.message;
    details = error.details;
    status = error.status;
  }

  console.log("handleCheckAvailabilityHandlerError is");
  console.log(error);
  console.log({
    errorCode,
    message,
    details,
  });

  return res.status(status).send({
    errorCode,
    message,
    details,
  });
}

function handleGetRoomsHandlerError(res: Response, error: any) {
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
