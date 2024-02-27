import { bookings } from "@/typings";
import axiosClient, { setAuthTokenForBookingService } from "./serviceConfig";

const bookingService = {
  createBookingRequest: (data: bookings.ICreateBookingRequest) => {
    setAuthTokenForBookingService();
    const url = `/request`;
    return axiosClient.post(url, data);
  },

  updateBookingRequestContactDetails: (
    data: bookings.IUpdateContactDetailsRequest
  ) => {
    setAuthTokenForBookingService();
    const url = `/request/${data.bookingRequestId}/contact-details`;
    return axiosClient.patch(url, data);
  },

  updateBookingRequestPaymentType: (
    data: bookings.IUpdatePaymentTypeRequest
  ) => {
    setAuthTokenForBookingService();
    const url = `/request/${data.bookingRequestId}/payment-type`;
    return axiosClient.patch(url, data);
  },

  getBookingRequest: (id: string) => {
    console.log("getting booking request data");

    setAuthTokenForBookingService();

    const url = `/request/${id}`;
    return axiosClient.get(url);
  },

  createBooking: (data: bookings.ICreateBooking) => {
    setAuthTokenForBookingService();
    const url = `/${data.bookingRequestId}`;
    return axiosClient.post(url, data);
  },

  getBookingDataByBookingRequestId: (bookingRequestId: string) => {
    setAuthTokenForBookingService();
    const url = ``;
    const params = { bookingRequestId: bookingRequestId };
    return axiosClient.get(url, {
      params,
    });
  },

  getBookingDataById: (bookingId: string) => {
    const url = `/${bookingId}`;
    return axiosClient.get(url);
  },

  getBookingsByUid: (uid: string) => {
    setAuthTokenForBookingService();
    const url = `/search`;
    return axiosClient.post(url, {
      uid: uid,
    });
  },
};

export default bookingService;
