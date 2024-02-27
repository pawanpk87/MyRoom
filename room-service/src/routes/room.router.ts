import express from "express";
import validate from "../middleware/validateResource";
import {
  createRoomSchema,
  deleteAllRoomSchema,
  deleteRoomSchema,
  getAllRoomQuerySchema,
  roomAvailabilitySchema,
  updateRoomSchema,
  updateRoomSchemaForBooking,
} from "../schema/room.schema";
import {
  checkAvailabilityHandler,
  createRoomHandler,
  deleteRoomHandler,
  deleteRoomsHandler,
  getRoomHandler,
  getRoomsHandler,
  updateRoomForBookingHandler,
  updateRoomHandler,
} from "../controller";

const roomRouter = express.Router();

roomRouter.post("", validate(createRoomSchema), createRoomHandler);

roomRouter.get("/:id", getRoomHandler);

roomRouter.patch("/:id", validate(updateRoomSchema), updateRoomHandler);

roomRouter.patch(
  "/:id/booking",
  validate(updateRoomSchemaForBooking),
  updateRoomForBookingHandler
);

roomRouter.delete("/:id", validate(deleteRoomSchema), deleteRoomHandler);

roomRouter.get("", getRoomsHandler);

roomRouter.delete("", validate(deleteAllRoomSchema), deleteRoomsHandler);

roomRouter.post(
  "/:id/availability",
  validate(roomAvailabilitySchema),
  checkAvailabilityHandler
);

export default roomRouter;
