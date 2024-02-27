import express from "express";
import validate from "../middleware/validateResource";
import { addReviewSchema, getAllReviewSchema } from "../schema";
import {
  addReviewHandler,
  getAllReviewByRoomHandler,
  getOverallRatingHandler,
  getReviewHandler,
} from "../controller";

const reviewRouter = express.Router();

/**
 * Add a new review for the room.
 */
reviewRouter.post("/:roomId", validate(addReviewSchema), addReviewHandler);

/**
 * Get review data.
 */
reviewRouter.get("/:id", getReviewHandler);

/**
 * Get review data.
 */
reviewRouter.get("/:roomId/overall-rating", getOverallRatingHandler);

/**
 * Get Reviews for a Room
 */
reviewRouter.get("", validate(getAllReviewSchema), getAllReviewByRoomHandler);

export default reviewRouter;
