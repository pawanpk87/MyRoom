import axiosClient, { setAuthTokenForBookingService } from "./serviceConfig";
import { bookings } from "@/typings";

const bookingService = {
  getBooking: (id: string) => {
    const url = `/${id}`;
    return axiosClient.get(url);
  },

  getBookings: (data: bookings.IGetBookingsQuery) => {
    const url = `/search`;
    return axiosClient.post(url, data);
  },

  updateBooking: (data: bookings.IBookingUpdateData) => {
    setAuthTokenForBookingService();
    const url = `/${data.bookingId}`;
    return axiosClient.patch(url, data);
  },

  getBookingStatistic: (params: bookings.IGetBookingStatisticQuery) => {
    const url = `/dashboard/statistics`;
    return axiosClient.get(url, { params });
  },

  getBookingsCount: (params: bookings.IGetBookingStatisticQuery) => {
    const url = `/dashboard/statistics/count`;
    return axiosClient.get(url, { params });
  },

  getGetPeopleCounts: (params: bookings.IGetPeopleCountsQuery) => {
    const url = `/dashboard/statistics/people-counts`;
    return axiosClient.get(url, { params });
  },

  getBookingRecords: (params: bookings.IGetBookingsRecordsQuery) => {
    const url = `/dashboard/records`;
    return axiosClient.get(url, { params });
  },
};

export default bookingService;
