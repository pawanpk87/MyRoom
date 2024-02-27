import { z } from "zod";
import {
  createReviewSchema,
  createRoomSchema,
  deleteAllRoomSchema,
  deleteRoomSchema,
  getAllRoomQuerySchema,
  getRoomsCountByStatusSchema,
  getTopPerformersRoomsQuerySchema,
  roomAvailabilitySchema,
  updateRoomSchema,
  updateRoomSchemaForBooking,
} from "./room.schema";

type roomCreateSchemaType = z.infer<typeof createRoomSchema>["body"];
type roomUpdateSchemaType = z.infer<typeof updateRoomSchema>["body"];
type roomDeleteSchemaType = z.infer<typeof deleteRoomSchema>["body"];
type roomAvailabilitySchemaType = z.infer<
  typeof roomAvailabilitySchema
>["body"];
type getAllRoomQuerySchemaType = z.infer<typeof getAllRoomQuerySchema>["query"];
type deleteAllRoomSchemaType = z.infer<typeof deleteAllRoomSchema>["body"];
type createReviewSchemaType = z.infer<typeof createReviewSchema>["body"];

type getRoomsCountByStatusSchemaType = z.infer<
  typeof getRoomsCountByStatusSchema
>["query"];

type updateRoomSchemaForBookingType = z.infer<
  typeof updateRoomSchemaForBooking
>["body"];

type getTopPerformersRoomsQuerySchemaType = z.infer<
  typeof getTopPerformersRoomsQuerySchema
>["query"];

export type createRoomInput = roomCreateSchemaType;

export type updateRoomInput = roomUpdateSchemaType;

export type deleteRoomInput = roomDeleteSchemaType;

export type availabilityInput = roomAvailabilitySchemaType;

export type getAllRoomQueryInput = getAllRoomQuerySchemaType;

export type deleteAllRoomInput = deleteAllRoomSchemaType;

export type createReviewInput = createReviewSchemaType;

export type getRoomsCountByStatusQueryInput = getRoomsCountByStatusSchemaType;

export type updateRoomSchemaForBookingTypeInput =
  updateRoomSchemaForBookingType;

export type getTopPerformersRoomsQueryInput =
  getTopPerformersRoomsQuerySchemaType;
