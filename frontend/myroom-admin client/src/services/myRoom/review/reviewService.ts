import axiosClient, { setAuthTokenForReveiwService } from "./serviceConfig";
import { reviews } from "@/typings/reviews";

const reviewService = {
  getReview: (id: string) => {
    const url = `/${id}`;
    return axiosClient.get(url);
  },

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
