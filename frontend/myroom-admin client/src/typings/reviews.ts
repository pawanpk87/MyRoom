export namespace reviews {
  export interface Review {
    id: string;
    roomId: string;
    organizationId: string;
    uid: string;
    reviewText: string;
    rating: number;
    createdAt: string;
  }

  export interface IReviewData {
    id: string;
    roomId: string;
    organizationId: string;
    uid: string;
    reviewText: string;
    rating: number;
  }

  export interface IGetReviewsQuery {
    roomId?: string;
    organizationId?: string;
    page?: number;
    limit?: number;
  }

  export interface IReviewQueryData {
    reviews: reviews.IReviewData[];
    totalRecods: number;
    totalPages: number;
    currentPage: number;
  }

  export interface IOverallRating {
    statistics: {
      "5star"?: number;
      "4star"?: number;
      "3star"?: number;
      "2star"?: number;
      "1star"?: number;
    };
    overall: number;
  }

  export interface IGetReviewsData {
    reviews: Review[];
    totalRecods: number;
    totalPages: number;
    currentPage: number;
  }
}
