import axiosClient, { setAuthTokenPaymentService } from "./serviceConfig";

const paymentService = {
  getPaymentDataByBookingId: (bookingId: string) => {
    setAuthTokenPaymentService();
    const url = `/orders/${bookingId}`;
    return axiosClient.get(url);
  },
};

export default paymentService;
