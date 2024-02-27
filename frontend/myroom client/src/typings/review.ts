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

  export interface IGetReviewsQuery {
    roomId: string;
  }

  export interface IGetReviewsData {
    reviews: Review[];
    totalRecods: number;
    totalPages: number;
    currentPage: number;
  }
}
