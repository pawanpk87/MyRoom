import { rooms } from "@/typings/rooms";
import axiosClient, { setAuthTokenForRoomService } from "./serviceConfig";

const roomService = {
  getRoom: (id: string) => {
    const url = `/${id}`;
    return axiosClient.get(url);
  },

  createRoom: (data: rooms.ICreateRoomData) => {
    setAuthTokenForRoomService();
    const url = ``;
    return axiosClient.post(url, data);
  },

  updateRoom: (data: rooms.IUpdateRoomData) => {
    setAuthTokenForRoomService();
    const url = `/${data.id}`;
    return axiosClient.patch(url, data);
  },

  deleteRoom: (data: rooms.IDeleteRoom) => {
    setAuthTokenForRoomService();
    const url = `/${data.id}`;
    return axiosClient.delete(url, { data });
  },

  getRooms: (params: rooms.IGetRoomsQuery) => {
    const url = ``;
    return axiosClient.get(url, { params });
  },

  deleteRooms: (data: rooms.IDeleteRooms) => {
    setAuthTokenForRoomService();
    const url = ``;
    return axiosClient.delete(url, { data });
  },

  getRoomsCount: (params: rooms.IGetRoomsCountQuery) => {
    const url = `dashboard/count`;
    return axiosClient.get(url, { params });
  },

  getTopPerformersRooms: (params: rooms.IGetTopPerformersRoomsQuery) => {
    const url = `dashboard/top-performers`;
    return axiosClient.get(url, { params });
  },
};

export default roomService;
