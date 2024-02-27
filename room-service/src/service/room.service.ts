import { AvailableDates, BookingStatus, CheckInType, Room } from "../model";
import {
  availabilityInput,
  createRoomInput,
  deleteAllRoomInput,
  deleteRoomInput,
  updateRoomInput,
  updateRoomSchemaForBookingTypeInput,
} from "../schema";
import RoomModel from "../model/room.model";
import ApiConstants from "../constants/ApiConstants";
import logger from "../config/logger";
import { IGetRooms, IRoomAvailability } from "./types";
import {
  validateCreateRoomData,
  validateDeleteRoomData,
  validateUpdateRoomData,
} from "./room.validation.service";
import { GenericValidInvalidEnum } from "../utils/types";

export async function createRoom(input: createRoomInput): Promise<Room> {
  logger.info(`Attempting room creation with input: `, input);

  // validate createRoom data
  const valid: GenericValidInvalidEnum = await validateCreateRoomData(input);

  if (valid === GenericValidInvalidEnum.VALID) {
    const newRoom: Room = {
      organizationId: input.organizationId,
      title: input.title,
      description: input.description,
      availableDates: input.availableDates,
      prices: input.prices,
      createdBy: input.uid,
      updatedBy: input.uid,
      note: input.note,
      capacity: input.capacity,
      bookingStatus: BookingStatus[input.bookingStatus],
      amenities: input.amenities,
      mainImage: input.mainImage,
      images: input.images,
      features: input.features,
      city: input.city,
      address: input.address,
      checkInTypes: input.checkInTypes.map((value) => CheckInType[value]),
    };
    return await RoomModel.create(newRoom);
  } else {
    throw {
      name: "AppError",
      errorCode: ApiConstants.INVALID_DATA,
      message: "Invalid room data",
      details: "",
      status: 400,
    };
  }
}

export async function updateRoom(input: updateRoomInput): Promise<Room | null> {
  logger.info(`Attempting room updation with input: `, input);

  // validate uodateRoom data
  const valid: GenericValidInvalidEnum = await validateUpdateRoomData(input);

  if (valid === GenericValidInvalidEnum.VALID) {
    let { uid, ...userGivenData } = input;

    return await RoomModel.findByIdAndUpdate(
      input.id,
      {
        $set: {
          ...userGivenData,
          updatedBy: uid,
        },
      },
      { new: true }
    );
  } else {
    throw {
      name: "AppError",
      errorCode: ApiConstants.INVALID_DATA,
      message: "Invalid room data",
      details: "",
      status: 400,
    };
  }
}

export async function updateRoomForBooking(
  input: updateRoomSchemaForBookingTypeInput
): Promise<Room | null> {
  logger.info(`Attempting room updation with input: `, input);

  // validate uodateRoom data
  const valid: GenericValidInvalidEnum = await validateUpdateRoomData(input);

  if (valid === GenericValidInvalidEnum.VALID) {
    let { uid, ...userGivenData } = input;

    return await RoomModel.findByIdAndUpdate(
      input.id,
      {
        $inc: { bookingCount: 1 },
        $set: {
          ...userGivenData,
          updatedBy: uid,
        },
      },
      { new: true }
    );
  } else {
    throw {
      name: "AppError",
      errorCode: ApiConstants.INVALID_DATA,
      message: "Invalid room data",
      details: "",
      status: 400,
    };
  }
}

export async function getRoom(id: string): Promise<Room | null> {
  return await RoomModel.findById(id);
}

export async function deleteRoom(input: deleteRoomInput) {
  logger.info(`Attempting to delete room with id: ${input.id}`);

  // validate delete room data
  const valid: GenericValidInvalidEnum = await validateDeleteRoomData(input);

  if (valid === GenericValidInvalidEnum.VALID) {
    return await RoomModel.findByIdAndDelete(input.id);
  } else {
    throw {
      name: "AppError",
      errorCode: ApiConstants.INVALID_DATA,
      message: "Invalid roomId data",
      details: "",
      status: 400,
    };
  }
}

export async function deleteRooms(input: deleteAllRoomInput) {
  logger.info(`Attempting to delete rooms with ids: ${input.ids}`);

  // validate delete room data
  const valid: GenericValidInvalidEnum = await validateDeleteRoomData({
    id: input.ids[0],
    uid: input.uid,
  });

  if (valid === GenericValidInvalidEnum.VALID) {
    return await RoomModel.deleteMany({
      _id: {
        $in: input.ids,
      },
    });
  } else {
    throw {
      name: "AppError",
      errorCode: ApiConstants.INVALID_DATA,
      message: "Invalid roomId data",
      details: "",
      status: 400,
    };
  }
}

export async function getRooms(
  page: number,
  limit: number,
  query: any
): Promise<IGetRooms> {
  const { sort = {}, ...findQuery } = query;
  const rooms: any = await RoomModel.find(findQuery)
    .sort(sort)
    .limit(limit * 1)
    .skip((page - 1) * limit)
    .exec();
  const count = await RoomModel.countDocuments(query);
  return {
    rooms,
    totalRecods: count,
    totalPages: Math.ceil(count / limit),
    currentPage: page,
  } as IGetRooms;
}

export async function checkRoomAvailability(
  id: string,
  input: availabilityInput
): Promise<IRoomAvailability> {
  const givenCheckInDate: Date = new Date(input.checkIn);
  const givenCheckOutDate: Date = new Date(input.checkOut);
  const guests: number = Number(input.guests);

  const room: any = await RoomModel.findById(id);

  if (!room) {
    const message = `couldn't find any room with this id: ${id}`;
    logger.error(message);
    throw {
      name: "AppError",
      errorCode: "RECORD_NOT_FOUND",
      message: `couldn't find any room with this id: ${id}`,
      details: `couldn't find any room with this id: ${id}`,
      status: 404,
    };
  }

  // check if room bookingStatus is AVAILABLE
  if (room.bookingStatus !== BookingStatus.AVAILABLE) {
    return {
      available: false,
      message: "Room is currently unavailable.",
    } as IRoomAvailability;
  }

  // check if given dates are within the range of record's startDate and endDate;
  const availableDates: AvailableDates = room.availableDates;
  if (
    !(
      givenCheckInDate >= availableDates.startDate &&
      givenCheckOutDate <= availableDates.endDate
    )
  ) {
    return {
      available: false,
      message: "Dates are outside the range",
    } as IRoomAvailability;
  }

  // check if given guests number within the range
  if (room.capacity < guests) {
    return {
      available: false,
      message: "Guests are outside the room's capacity",
    } as IRoomAvailability;
  }

  return {
    available: true,
    message: "available",
  } as IRoomAvailability;
}
