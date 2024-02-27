import express from "express";
import validate from "../middleware/validateResource";
import {
  createOrganizationHandler,
  getOrganizationByUserHandler,
  getOrganizationHandler,
  getOrganizationsHandler,
  updateOrganizationHandler,
  verifyUserHandler,
} from "../controller/organization.controller";
import {
  createOrganizationSchema,
  getAllOrganizationsQuerySchema,
  updateOrganizationSchema,
} from "../schema/organization.schema";

const organizationRouter = express.Router();

organizationRouter.post(
  "/org",
  validate(createOrganizationSchema),
  createOrganizationHandler
);

organizationRouter.get("/org/:id", getOrganizationHandler);

organizationRouter.get("/org/user/:uid", getOrganizationByUserHandler);

organizationRouter.patch(
  "/org/:id",
  validate(updateOrganizationSchema),
  updateOrganizationHandler
);

organizationRouter.get("/org/:id/permissions/:uid", verifyUserHandler);

organizationRouter.get(
  "/org",
  validate(getAllOrganizationsQuerySchema),
  getOrganizationsHandler
);

export default organizationRouter;
