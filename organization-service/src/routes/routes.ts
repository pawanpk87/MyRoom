import { Express } from "express";
import ApiConstants from "../constants/ApiConstants";
import organizationRouter from "./organization.router";

function routes(app: Express) {
  app.use(`${ApiConstants.ORGANIZATION_SERVICE_API_V1}`, organizationRouter);
}

export default routes;
