import { Room } from "../model";
import RoomModel from "../model/room.model";
import {
  getRoomsCountByStatusQueryInput,
  getTopPerformersRoomsQueryInput,
} from "../schema";

export async function getRoomsCount(
  input: getRoomsCountByStatusQueryInput
): Promise<number> {
  const query = {
    organizationId: input.organizationId,
    bookingStatus: input.bookingStatus,
  };

  const count = await RoomModel.countDocuments(query);

  return count;
}

export async function getTopPerformerRooms(
  input: getTopPerformersRoomsQueryInput
): Promise<Room[]> {
  const query = {
    organizationId: input.organizationId,
  };

  const rooms: Room[] = await RoomModel.find(query)
    .sort({
      bookingCount: -1,
    })
    .limit(3);

  return rooms;
}
