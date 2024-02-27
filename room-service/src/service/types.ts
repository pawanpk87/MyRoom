import { createReviewInput } from "../schema";

export interface IGetRooms {
  rooms: any;
  totalRecods: number;
  totalPages: number;
  currentPage: number;
}

export interface IRoomAvailability {
  available: boolean;
  message: string;
}

export enum AdminType {
  SUPER_ADMIN,
  ADMIN,
}

export interface Admin {
  uid: string;
  adminType: AdminType;
}

export interface IOrganizationData {
  name: string;
  superAdmin: Admin | null;
  admins: Admin[];
}

export interface ICreatedReview {
  review: any;
  sumOfRatings: number;
  totalReviewCount: number;
}
