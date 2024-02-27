import express from "express";
import {
  getRoomsCountByStatusSchema,
  getTopPerformersRoomsQuerySchema,
} from "../schema";
import validate from "../middleware/validateResource";
import {
  getPerformersRoomsHandler,
  getRoomsCountHandler,
} from "../controller/room.dashboard.controller";

const roomDashboardRouter = express.Router();

roomDashboardRouter.get(
  "/count",
  validate(getRoomsCountByStatusSchema),
  getRoomsCountHandler
);

roomDashboardRouter.get(
  "/top-performers",
  validate(getTopPerformersRoomsQuerySchema),
  getPerformersRoomsHandler
);

export default roomDashboardRouter;
