import express from "express";
import validate from "../middleware/validateResource";
import { createReviewSchema } from "../schema";
import { createReviewHandler } from "../controller";

const roomReviewRouter = express.Router();

roomReviewRouter.post(
  "/:roomId",
  validate(createReviewSchema),
  createReviewHandler
);

export default roomReviewRouter;
