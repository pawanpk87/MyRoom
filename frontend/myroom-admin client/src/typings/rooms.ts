export namespace rooms {
  export interface AvailableDates {
    startDate: Date;
    endDate: Date;
  }

  export enum Currency {
    INR,
  }

  export interface Prices {
    price: number;
    cleaningFee: number;
    roomService: number;
    currency: string;
  }

  export interface Rating {
    rating: number;
    outOf: number;
  }

  export enum CheckInType {
    ONLINE_PAYMENT = "ONLINE_PAYMENT",
    PAY_AT_HOTEL = "PAY_AT_HOTEL",
  }

  export enum AdminType {
    SUPER_ADMIN,
    ADMIN,
  }

  export interface Admin {
    uid: string;
    adminType: AdminType;
  }

  export enum BookingStatus {
    AVAILABLE = "AVAILABLE",
    BOOKED = "BOOKED",
    OCCUPIED = "OCCUPIED",
    MAINTENANCE = "MAINTENANCE",
  }

  export interface IRoomData {
    id: string;
    organizationId: string;
    title: string;
    description: string;
    availableDates: AvailableDates;
    prices: Prices;
    cost: number;
    createdBy?: string;
    updatedBy?: string;
    note?: string | null | undefined;
    capacity: number;
    rating?: Rating;
    bookingStatus: BookingStatus;
    reviewId?: string;
    amenities: string[];
    mainImage: string;
    images: string[];
    features: string[];
    city: string;
    address: string;
    checkInTypes: CheckInType[];
    bookingCount?: number;
  }

  export interface ICreateRoomData
    extends Omit<
      IRoomData,
      | "id"
      | "cost"
      | "createdBy"
      | "updatedBy"
      | "rating"
      | "reviewId"
      | "images"
    > {
    uid: string;
  }

  export interface IUpdateRoomData
    extends Omit<
      IRoomData,
      | "organizationId"
      | "cost"
      | "createdBy"
      | "updatedBy"
      | "rating"
      | "reviewId"
      | "images"
    > {
    uid: string;
  }

  export interface IDeleteRoom {
    id: string;
    uid: string;
  }

  export interface IDeleteRooms {
    ids: string[];
    uid: string;
  }

  export enum BookingStatusEnum {
    AVAILABLE = "AVAILABLE",
    BOOKED = "BOOKED",
    OCCUPIED = "OCCUPIED",
    MAINTENANCE = "MAINTENANCE",
  }

  export enum CheckInTypeEnum {
    ONLINE_PAYMENT = "ONLINE_PAYMENT",
    PAY_AT_HOTEL = "PAY_AT_HOTEL",
  }

  export enum QueryRelationalOperatorEnum {
    EQUAL = "EQUAL",
    NOT_EQUAL = "NOT_EQUAL",
    GREATER_THAN = "GREATER_THAN",
    LESS_THAN = "LESS_THAN",
    GREATER_THAN_OR_EQUAL = "GREATER_THAN_OR_EQUAL",
    LESS_THAN_OR_EQUAL = "LESS_THAN_OR_EQUAL",
    IN = "IN",
  }

  export interface IGetRoomsQuery {
    organizationId?: string;
    title?: string;
    availableDates?: {
      startDate: {
        operator: QueryRelationalOperatorEnum;
        startDate: string;
      };
      endDate: {
        operator: QueryRelationalOperatorEnum;
        endDate: string;
      };
    };
    cost?: {
      operator: QueryRelationalOperatorEnum;
      cost: number;
    };
    rating?: {
      rating: {
        operator: QueryRelationalOperatorEnum;
        rating: number;
      };
      outOf: {
        operator: QueryRelationalOperatorEnum;
        rating: number;
      };
    };
    capacity?: {
      operator: QueryRelationalOperatorEnum;
      capacity: number;
    };
    bookingStatus?: BookingStatusEnum;
    amenities?: string[];
    features?: string[];
    city?: string;
    checkInTypes?: CheckInTypeEnum;
    sorting?: {
      operator: number;
      field: string;
    }[];
    page?: number;
    limit?: number;
  }

  export interface IQueryData {
    rooms: rooms.IRoomData[];
    totalRecods: number;
    totalPages: number;
    currentPage: number;
  }

  export interface IGetRoomsCountData {
    count: number;
  }

  export interface IGetRoomsCountQuery {
    organizationId: string;
    bookingStatus: BookingStatus;
  }

  export interface IGetTopPerformersRoomsQuery {
    organizationId: string;
  }
}
