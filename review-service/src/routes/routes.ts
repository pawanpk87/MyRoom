import { Express } from "express";
import ApiConstants from "../constants/ApiConstants";
import reviewRouter from "./review.router";

function routes(app: Express) {
  app.use(`${ApiConstants.REVIEW_SERVICE_API_V1}`, reviewRouter);
}

export default routes;
