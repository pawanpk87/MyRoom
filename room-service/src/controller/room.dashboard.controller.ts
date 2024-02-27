import { Request, Response } from "express";
import logger from "../config/logger";
import {
  getRoomsCountByStatusQueryInput,
  getTopPerformersRoomsQueryInput,
} from "../schema";
import ApiConstants from "../constants/ApiConstants";
import {
  getRoomsCount,
  getTopPerformerRooms,
} from "../service/room.dashboard.service";
import { Room } from "../model";
import { mapKeys, omit } from "lodash";

export async function getRoomsCountHandler(
  req: Request<{}, {}, {}, getRoomsCountByStatusQueryInput>,
  res: Response
) {
  logger.info(`Received get all rooms count`);
  logger.info(`The query is: `, req.query);

  const query = req.query;

  try {
    logger.info("Fetching rooms count");
    const count: number = await getRoomsCount(query);
    logger.info(`Fetched rooms count: ${count}`);

    res.status(200).send({
      count: count,
    });
  } catch (error) {
    logger.error(`Error occured while fetching rooms: `, error);
    handleGetRoomsHandlerError(res, error);
  }
}

export async function getPerformersRoomsHandler(
  req: Request<{}, {}, {}, getTopPerformersRoomsQueryInput>,
  res: Response
) {
  logger.info(`Received get top-performers rooms request`);
  logger.info(`The query is: `, req.query);

  const query = req.query;

  try {
    logger.info("Fetching top-performers rooms");
    const rooms: Room[] = await getTopPerformerRooms(query);
    logger.info(`Fetched top-performers rooms: ${rooms}`);

    const allRooms = rooms.map((room: any) => {
      return mapKeys(
        omit(JSON.parse(JSON.stringify(room)), ["__v"]),
        (value, key) => {
          return key === "_id" ? "id" : key;
        }
      );
    });

    res.status(200).send(allRooms);
  } catch (error) {
    logger.error(`Error occured while fetching rooms: `, error);
    handleGetRoomsHandlerError(res, error);
  }
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
