export interface AvailableDates {
  startDate: Date;
  endDate: Date;
}

export enum Currency {
  INR,
}

export interface Prices {
  price: Number;
  cleaningFee: Number;
  roomService: Number;
  currency: String;
}

export interface Rating {
  rating: Number | null;
  outOf: Number;
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

export interface Room {
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
  checkInTypes: CheckInType[];
  bookingCount?: number;
}
