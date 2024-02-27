import { Express } from "express";
import ApiConstants from "../constants/ApiConstants";
import roomRouter from "./room.router";
import roomReviewRouter from "./room.reviews.router";
import roomDashboardRouter from "./room.dashboard";

function routes(app: Express) {
  app.use(`${ApiConstants.ROOM_SERVICE_API_V1}`, roomRouter);

  app.use(`${ApiConstants.ROOM_SERVICE_API_V1}/reviews`, roomReviewRouter);

  app.use(`${ApiConstants.ROOM_SERVICE_API_V1}/dashboard`, roomDashboardRouter);
}

export default routes;
