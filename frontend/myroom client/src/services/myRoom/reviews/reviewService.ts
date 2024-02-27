import { reviews } from "@/typings/review";
import axiosClient, { setAuthTokenReviewService } from "./serviceConfig";

const reviewService = {
  getReviews: (params: reviews.IGetReviewsQuery) => {
    const url = ``;
    return axiosClient.get(url, { params });
  },

  getOverallRating: (roomId: string) => {
    const url = `/${roomId}/overall-rating`;
    return axiosClient.get(url);
  },
};

export default reviewService;
