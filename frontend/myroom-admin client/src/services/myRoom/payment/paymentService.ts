import { payments } from "@/typings";
import axiosClient, { setAuthTokenForPaymentService } from "./serviceConfig";

const paymentService = {
  getPaymentStatistic: (params: payments.IGetPaymentStatisticQuery) => {
    setAuthTokenForPaymentService();
    const url = `/dashboard/statistics`;
    return axiosClient.get(url, { params });
  },

  getRevenuStatistic: (params: payments.IGetRevenueStatisticQuery) => {
    setAuthTokenForPaymentService();
    const url = `/dashboard/statistics/revenue`;
    return axiosClient.get(url, { params });
  },
};

export default paymentService;
