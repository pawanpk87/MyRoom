import { rooms } from "@/typings/room";
import axiosClient, { setAuthTokenRoomService } from "./serviceConfig";

const roomService = {
  getRooms: (params: rooms.IGetRoomsQuery) => {
    params.bookingStatus = rooms.BookingStatusEnum.AVAILABLE;
    const url = ``;
    return axiosClient.get(url, { params });
  },

  getRoom: (id: string) => {
    const url = `/${id}`;
    return axiosClient.get(url);
  },

  addReview: (data: rooms.IAddReview) => {
    setAuthTokenRoomService();
    const url = `/reviews/${data.roomId}`;
    return axiosClient.post(url, data);
  },
};

export default roomService;
