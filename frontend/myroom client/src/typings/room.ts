export namespace rooms {
  export interface AvailableDates {
    startDate: Date;
    endDate: Date;
  }

  export enum Currency {
    INR = "INR",
  }

  export interface Prices {
    price: number;
    cleaningFee: number;
    roomService: number;
    currency: string;
  }

  export interface Rating {
    rating: number | null;
    outOf: number;
  }

  export enum CheckInType {
    ONLINE_PAYMENT = "ONLINE_PAYMENT",
    PAY_AT_HOTEL = "PAY_AT_HOTEL",
  }

  export enum AdminType {
    SUPER_ADMIN = "SUPER_ADMIN",
    ADMIN = "ADMIN",
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
    cost?: number;
    createdBy?: string;
    updatedBy?: string;
    note?: string | null | undefined;
    capacity: number;
    rating?: Rating | null;
    bookingStatus: BookingStatus;
    reviewId?: string;
    amenities: string[];
    mainImage: string;
    images: string[];
    features: string[];
    city: string;
    address: string;
    checkInType: CheckInType;
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
    checkInType?: CheckInTypeEnum;
    page?: number;
    limit?: number;
  }

  export interface IAddReview {
    roomId: string;
    organizationId: string;
    uid: string;
    reviewText: string;
    rating: number;
  }
}
