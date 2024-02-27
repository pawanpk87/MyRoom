import { Request, Response } from "express";
import logger from "../config/logger";
import { createReviewInput } from "../schema";
import { createReview } from "../service/room.review.service";
import ApiConstants from "../constants/ApiConstants";

export async function createReviewHandler(
  req: Request<{ roomId: string }, {}, createReviewInput>,
  res: Response
) {
  logger.info(
    `Received review creation request for roomId: ${req.params.roomId}`
  );

  const review: createReviewInput = req.body;
  review.roomId = req.params.roomId;

  try {
    logger.info(`Creating review  for: `, review);
    const result: any = await createReview(review);
    logger.info(`Review created successfully: `, result);

    return res.status(201).send(result);
  } catch (error) {
    logger.error(`Error occurred while creating review`, error);
    return handleCreateReviewHandlerError(res, error);
  }
}

function handleCreateReviewHandlerError(res: Response, error: any) {
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
