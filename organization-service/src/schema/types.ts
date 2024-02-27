import { z } from "zod";
import {
  createOrganizationSchema,
  getAllOrganizationsQuerySchema,
  updateOrganizationSchema,
} from "./organization.schema";

type organizationCreateSchemaType = z.infer<
  typeof createOrganizationSchema
>["body"];
type organizationUpdateSchemaType = z.infer<
  typeof updateOrganizationSchema
>["body"];

type getAllOrganizationsSchemaType = z.infer<
  typeof getAllOrganizationsQuerySchema
>["body"];

export type createOrganizationInput = organizationCreateSchemaType;
export type updateOrganizationInput = organizationUpdateSchemaType;
export type getAllOrganizationsInput = getAllOrganizationsSchemaType;
